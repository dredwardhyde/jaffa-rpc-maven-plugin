[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bfa0c04380ff44c69c51550add0f3116)](https://app.codacy.com/manual/dredwardhyde/jaffa-rpc-maven-plugin?utm_source=github.com&utm_medium=referral&utm_content=dredwardhyde/jaffa-rpc-maven-plugin&utm_campaign=Badge_Grade_Dashboard)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.dredwardhyde/jaffa-rpc-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.dredwardhyde/jaffa-rpc-maven-plugin)
[![javadoc](https://javadoc.io/badge2/com.github.dredwardhyde/jaffa-rpc-maven-plugin/javadoc.svg)](https://javadoc.io/doc/com.github.dredwardhyde/jaffa-rpc-maven-plugin)
[![build](https://github.com/dredwardhyde/jaffa-rpc-maven-plugin/workflows/build/badge.svg)](https://github.com/dredwardhyde/jaffa-rpc-maven-plugin/actions)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdredwardhyde%2Fjaffa-rpc-maven-plugin.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdredwardhyde%2Fjaffa-rpc-maven-plugin?ref=badge_shield)
## How to use jaffa-rpc-maven-plugin

This maven plugin is a part of [Jaffa RPC library](https://github.com/dredwardhyde/jaffa-rpc-library) project and supposed to create client interfaces.  
To turn off sources generation - disable corresponding maven profile (like "generate-client-api" in example below):

```xml
<properties>
    <root-path>src/main/java/</root-path>
</properties>

<profiles>
        <profile>
            <id>generate-client-api</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>com.github.dredwardhyde</groupId>
                        <artifactId>jaffa-rpc-maven-plugin</artifactId>
                        <version>1.1/version>
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


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fdredwardhyde%2Fjaffa-rpc-maven-plugin.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fdredwardhyde%2Fjaffa-rpc-maven-plugin?ref=badge_large)
