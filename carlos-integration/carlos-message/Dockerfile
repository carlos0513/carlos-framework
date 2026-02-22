FROM harbor01.carlos.com/library/openjdk:8-jdk-bbt-alpine3.9

RUN mkdir -p /jar

WORKDIR /jar

# 默认端口8080
ENV PORT=8080

EXPOSE ${PORT}

ENV JAVA_OPTS "-Xmx512m -Xms256m  -Djava.security.egd=file:/dev/./urandom"

ENV PROJECT_NAME "carlos-message"

ENV JAR_NAME "carlos-message"

ADD ./${PROJECT_NAME}-cloud/target/${JAR_NAME}.jar ./

CMD java -server ${JAVA_OPTS} -jar ${JAR_NAME}.jar
