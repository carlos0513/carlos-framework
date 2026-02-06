<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.carlos</groupId>
        <artifactId>carlos</artifactId>
        <version>2.2.3.RELEASE</version>
    </parent>
    <packaging>pom</packaging>

    <version>${r'${revision}'}</version>
    <groupId>${maven.groupId}</groupId>
    <artifactId>${maven.artifactId}</artifactId>
    <name>${maven.artifactId}</name>
    <description>${maven.describe}</description>


    <modules>
        <module>${maven.artifactId}-bus</module>
        <module>${maven.artifactId}-api</module>
        <module>${maven.artifactId}-cloud</module>
        <module>${maven.artifactId}-boot</module>
    </modules>
    <modelVersion>4.0.0</modelVersion>

    <properties>
        <revision>1.0.0-SNAPSHOT</revision>
    </properties>


    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>${maven.groupId}</groupId>
                <artifactId>${maven.artifactId}-bus</artifactId>
                <version>${r'${revision}'}</version>
            </dependency>
            <dependency>
                <groupId>${maven.groupId}</groupId>
                <artifactId>${maven.artifactId}-api</artifactId>
                <version>${r'${revision}'}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

</project>