package com.transport.lib.plugin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import com.github.javaparser.ast.Modifier;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

@Mojo(name = "generate-transport-api")
@SuppressWarnings("unused")
public class TransportPlugin extends AbstractMojo {

    private static HashMap<String, String> PRIMITIVE_TO_CLASS = new HashMap<>();
    private static String API_ANNOTATION_NAME = "Api";
    private static String API_CLIENT_ANNOTATION_NAME = "ApiClient";
    private static String API_ANNOTATION_PACKAGE = "com.transport.lib.zeromq";
    private static String API_CLIENT_ANNOTATION_PACKAGE = "com.transport.lib.zeromq";
    private static String API_ANNOTATION_FULL = API_ANNOTATION_PACKAGE + "." + API_ANNOTATION_NAME;
    private static String API_CLIENT_ANNOTATION_FULL = API_CLIENT_ANNOTATION_PACKAGE + "." + API_CLIENT_ANNOTATION_NAME;
    private static String JAVA_EXTENSION = ".java";
    private static String REQUEST_INTERFACE_NAME = "RequestInterface";
    private static String REQUEST_INTERFACE_PACKAGE = "com.transport.lib.zeromq";
    private static String REQUEST_INTERFACE_FULL = REQUEST_INTERFACE_PACKAGE + "." +  REQUEST_INTERFACE_NAME;

    static {
        PRIMITIVE_TO_CLASS.put("int", "Integer");
        PRIMITIVE_TO_CLASS.put("void", "Void");
        PRIMITIVE_TO_CLASS.put("byte", "Byte");
        PRIMITIVE_TO_CLASS.put("boolean", "Boolean");
        PRIMITIVE_TO_CLASS.put("long", "Long");
        PRIMITIVE_TO_CLASS.put("double", "Double");
        PRIMITIVE_TO_CLASS.put("float", "Float");
        PRIMITIVE_TO_CLASS.put("short", "Short");
        PRIMITIVE_TO_CLASS.put("char", "Character");
    }

    @Parameter(property = "root-path",defaultValue = "src/main/java/")
    private String root;

    public void execute() throws MojoExecutionException {
        try{
            File rootFolder = new File(root);
            if(!rootFolder.exists()) throw new IllegalArgumentException("Root " + root + " doesn't exist");
            if(!rootFolder.isDirectory()) throw new IllegalArgumentException("Root " + root + " is not a folder");
            processFolder(rootFolder, getLog());
        }catch (Exception e){
            throw new MojoExecutionException("Error during transport api generation: ", e);
        }

    }

    private static void processFolder(File folder, Log log) throws Exception{
        File[] classes = folder.listFiles((file, name) -> name.endsWith(JAVA_EXTENSION));
        if(classes != null){
            for(File clazz : classes){
                CompilationUnit cu = JavaParser.parse(clazz);
                Optional<String> mainClass = cu.getPrimaryTypeName();
                if(mainClass.isPresent()){
                    Optional<ClassOrInterfaceDeclaration> cl = cu.getInterfaceByName(mainClass.get());
                    if(cl.isPresent()){
                        ClassOrInterfaceDeclaration classOrInterfaceDeclaration =  cl.get();
                        Optional<AnnotationExpr> annotationExpr = classOrInterfaceDeclaration.getAnnotationByName(API_ANNOTATION_NAME);
                        if(annotationExpr.isPresent()){
                            log.info("Processing " + classOrInterfaceDeclaration.getName() + JAVA_EXTENSION);
                            annotationExpr.get().setName(API_CLIENT_ANNOTATION_NAME);
                            classOrInterfaceDeclaration.setName(classOrInterfaceDeclaration.getName() + "Transport");
                            cu.accept(new FieldVisitor(), null);
                            cu.accept(new MethodVisitor(), null);
                            cu.accept(new ImportVisitor(), null);
                            ImportDeclaration id = new ImportDeclaration(REQUEST_INTERFACE_FULL, false, false);
                            List<ImportDeclaration> idList = new ArrayList<>();
                            if(cu.getImports()!=null) idList.addAll(cu.getImports());
                            if(!idList.contains(id)) cu.addImport(id);
                            File result = new File(folder, classOrInterfaceDeclaration.getName() + JAVA_EXTENSION);
                            boolean created = result.createNewFile();
                            if(!created) log.info("File " + classOrInterfaceDeclaration.getName() + JAVA_EXTENSION + " updated");
                            PrintStream stream = new PrintStream(result, "UTF-8");
                            stream.println(cu.toString());
                        }
                    }
                }
            }
        }

        File[] directories = folder.listFiles(File::isDirectory);
        if(directories != null){
            for (File directory : directories){
                processFolder(directory, log);
            }
        }
    }

    private static class MethodVisitor extends ModifierVisitor<Void> {
        @Override
        public Node visit(MethodDeclaration n, Void arg) {

            if(n.getModifiers().contains(Modifier.STATIC) || n.getModifiers().contains(Modifier.DEFAULT)) {
                return null;
            }
            if(PRIMITIVE_TO_CLASS.containsKey(n.getType().asString())){
                n.setType(REQUEST_INTERFACE_NAME + "<" +  PRIMITIVE_TO_CLASS.get(n.getType().asString())+ ">");
            }else{
                n.setType(REQUEST_INTERFACE_NAME + "<" +  n.getType()+ ">");
            }
            return n;
        }
    }

    private  static class FieldVisitor extends ModifierVisitor<Void> {
        @Override
        public Node visit(VariableDeclarator declarator, Void args) {
            return null;
        }
    }

    public static class ImportVisitor extends ModifierVisitor<Void> {
        @Override
        public Node visit(ImportDeclaration im, Void arg) {
            if(im.getName().asString().equals(API_ANNOTATION_FULL)) im.setName(API_CLIENT_ANNOTATION_FULL);
            return im;
        }
    }
}
