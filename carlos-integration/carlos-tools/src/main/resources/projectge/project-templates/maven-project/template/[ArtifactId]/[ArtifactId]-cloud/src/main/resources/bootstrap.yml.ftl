server:
  port: 9519
spring:
  profiles:
    active: ${r'${PROFILE:local}'}
  application:
name: carlos-szt-docking
  cloud:
    nacos:
      discovery:
        enabled: true
      config:
        enabled: true
        file-extension: yml
        refresh-enabled: true
        shared-configs:
          -
            data-id: share-config.yml
            refresh: true
          -
            data-id: redis.yml
            refresh: true
          -
            data-id: mysql.yml
            refresh: true
          -
            data-id: minio.yml
            refresh: true

# 本地调试环境
---
spring:
  config:
    activate:
      on-profile: local
  cloud:
    nacos:
      username: ${r'${NACOS_USERNAME:@nacos.username@}'}
      password: ${r'${NACOS_PASSWORD:@nacos.password@}'}
      discovery:
        server-addr: ${r'${NACOS_ADDR:@nacos.addr@}'}
        namespace: ${r'${NACOS_NAMESPACE:@nacos.namespace@}'}
        group: ${r'${NACOS_GROUP:@nacos.group@}'}
      config:
        server-addr: ${r'${NACOS_ADDR:@nacos.addr@}'}
        namespace: ${r'${NACOS_NAMESPACE:@nacos.namespace@}'}
        group: ${r'${NACOS_GROUP:@nacos.group@}'}


# 部署采用配置文件
---
spring:
  config:
    activate:
      on-profile: deploy
  cloud:
    nacos:
      username: ${r'${NACOS_USERNAME}'}
      password: ${r'${NACOS_PASSWORD}'}
      discovery:
        server-addr: ${r'${NACOS_ADDR:127.0.0.1:8848}'}
        namespace: ${r'${NACOS_NAMESPACE}'}
        group: ${r'${NACOS_GROUP}'}
      config:
        namespace: ${r'${NACOS_NAMESPACE}'}
        server-addr: ${r'${NACOS_ADDR}'}
        group: ${r'${NACOS_GROUP}'}
