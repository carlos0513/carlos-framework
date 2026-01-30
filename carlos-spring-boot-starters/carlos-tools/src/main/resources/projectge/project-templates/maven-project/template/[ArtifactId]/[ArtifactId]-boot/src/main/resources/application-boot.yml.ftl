# spring配置
spring:
datasource:
dynamic:
primary: master
datasource:
# 主库数据源
master:
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://${MYSQL_URL}?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
username: ${MYSQL_USERNAME}
password: ${MYSQL_PWD}
# 动态表数据源
dynamic_db:
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://${MYSQL_URL_SLAVE}?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
username: ${MYSQL_USERNAME_SLAVE}
password: ${MYSQL_PWD_SLAVE}


---
carlos:
##############################  minio配置 start  ###########################
minio:
enabled: true
endpoint: ${MINIO_HOST}
access-key: ${MINIO_USERNAME}
secret-key: ${MINIO_PWD}
public-endpoint: ${MINIO_PUBLIC_HOST}
bucket: carlos-default
connect-timeout: 10s
write-timeout: 60s
read-timeout: 10s
check-bucket: true
create-bucket: true
##############################  minio配置 end   ###########################

###############################  缓存数据库配置 #################################
---
spring:
cache:
type: redis
redis:
database: ${REDIS_DB}
host: ${REDIS_IP}
port: ${REDIS_PORT}
password: ${REDIS_PWD}
lettuce:
pool:
max-active: 50
max-idle: 100
max-wait: 30s
min-idle: 0
shutdown-timeout: 100ms
##############################  redis缓存配置 end   ###########################
