FROM 192.168.23.212:32000/yunjin-docker/library/openjdk:8-jdk-alpine3.9

RUN mkdir -p /jar

WORKDIR /jar

# 默认端口8080
ENV PORT=8080

EXPOSE ${PORT}

ENV JAVA_OPTS "-Xmx1024m -Xms1024m"

ENV PROJECT_NAME="carlos-auth"

ADD ./${PROJECT_NAME}-cloud/target/${PROJECT_NAME}.jar ./${PROJECT_NAME}.jar

CMD java -server ${JAVA_OPTS} -Dserver.port=${PORT} -jar ${PROJECT_NAME}.jar
