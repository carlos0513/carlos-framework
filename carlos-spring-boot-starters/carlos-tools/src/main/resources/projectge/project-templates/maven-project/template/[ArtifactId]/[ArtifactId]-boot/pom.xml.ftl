<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>${maven.groupId}</groupId>
        <artifactId>${maven.artifactId}</artifactId>
        <version>${r'${revision}'}</version>
    </parent>

    <artifactId>${maven.artifactId}-boot</artifactId>


    <dependencies>
        <dependency>
            <groupId>${maven.groupId}</groupId>
            <artifactId>${maven.artifactId}-bus</artifactId>
        </dependency>
        <dependency>
            <groupId>com.carlos</groupId>
            <artifactId>carlos-springboot</artifactId>
        </dependency>
    </dependencies>

    <build>
        <finalName>${r'${project.parent.artifactId}'}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>