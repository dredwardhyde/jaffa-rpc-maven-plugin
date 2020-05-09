## How to use transport-maven-plugin

This plugin is a part of Jaffa RPC library project and supposed to 
create client interfaces.  
To turn off sources generation - disable corresponding maven profile (like "generate-client-api" in example below):

```
<properties>
    <root-path>src/main/java/</root-path>
</properties>

<profiles>
        <profile>
            <id>generate-client-api</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>com.edwardhyde</groupId>
                        <artifactId>jaffa-rpc-maven-plugin</artifactId>
                        <version>1.0-SNAPSHOT</version>
                        <configuration>
                            <root-path>${root-path}</root-path>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>generate-client-api</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```
