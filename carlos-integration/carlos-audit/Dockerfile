FROM openjdk:8-jdk-alpine3.9

ENV LANG en_US.UTF-8

# 设置镜像源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories

# 解决时差8小时问题
RUN apk add tzdata
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 安装字体
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
#安装simsun
RUN wget -O /usr/share/fonts/simsun.ttf https://pfh-file-store.oss-cn-hangzhou.aliyuncs.com/simsun.ttf
RUN fc-cache -vf

RUN mkdir -p /jar

WORKDIR /jar

# 默认端口8080
ENV PORT=8080

EXPOSE ${PORT}

ENV JAVA_OPTS "-Xmx1024m -Xms256m"

ENV PROJECT_NAME="${project.artifactId}"

ADD ./${PROJECT_NAME}-cloud/target/${PROJECT_NAME}.jar ./${PROJECT_NAME}.jar

CMD java -server ${JAVA_OPTS} -Dserver.port=${PORT} -jar ${PROJECT_NAME}.jar
