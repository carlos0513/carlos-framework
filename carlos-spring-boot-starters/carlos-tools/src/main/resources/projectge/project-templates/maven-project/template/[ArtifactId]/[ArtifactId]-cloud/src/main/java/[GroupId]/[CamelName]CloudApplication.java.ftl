package ${maven.groupId};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
* 启动程序
*
* @author carlos
*/
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ${maven.camelName}CloudApplication {

public static void main ( final String[] args){
SpringApplication.run(${maven.camelName}CloudApplication.class, args);
}
}
