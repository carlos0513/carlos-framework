FROM harbor01.carlos.com/library/openjdk:8-jdk-bbt-alpine3.9

RUN mkdir -p /jar

WORKDIR /jar

# 默认端口8080
ENV PORT=8080

EXPOSE ${PORT}

ENV JAVA_OPTS "-Xmx2048m -Xms1024m -Djava.security.egd=file:/dev/./urandom"

ENV PROJECT_NAME "carlos-system"

ADD ./${PROJECT_NAME}-cloud/target/${PROJECT_NAME}.jar ./

CMD java -server ${JAVA_OPTS} -Dserver.port=${PORT} -jar ${PROJECT_NAME}.jar
