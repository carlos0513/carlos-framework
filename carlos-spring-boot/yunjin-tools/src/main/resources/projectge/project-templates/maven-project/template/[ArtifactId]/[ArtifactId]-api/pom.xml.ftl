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

    <artifactId>${maven.artifactId}-api</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.yunjin</groupId>
            <artifactId>yunjin-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-openfeign-core</artifactId>
        </dependency>
    </dependencies>
</project>