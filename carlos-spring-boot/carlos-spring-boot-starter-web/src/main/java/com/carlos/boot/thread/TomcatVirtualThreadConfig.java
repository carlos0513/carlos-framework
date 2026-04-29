package com.carlos.boot.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Tomcat 虚拟线程（Virtual Threads）配置。
 *
 * <p>Java 21 虚拟线程（JEP 444）与 Tomcat 集成：将 HTTP 请求处理线程池
 * 替换为 {@code Executors.newVirtualThreadPerTaskExecutor()}，使得每个请求
 * 都在独立的虚拟线程中执行，可轻松支撑数十万级并发连接。</p>
 *
 * <p>Spring Boot 3.2+ 可通过 {@code spring.threads.virtual.enabled=true} 自动启用，
 * 本配置类提供显式控制，确保在复杂部署环境下仍能可靠生效。</p>
 *
 * <p><b>生效前提</b>：</p>
 * <ul>
 *   <li>JDK 21+（启用虚拟线程）</li>
 *   <li>使用 Tomcat 作为嵌入式 Servlet 容器（本框架默认）</li>
 * </ul>
 *
 * @author carlos
 * @since 3.0.0
 * @see VirtualThreadConfig
 */
@Slf4j
@Configuration
@ConditionalOnClass(TomcatProtocolHandlerCustomizer.class)
public class TomcatVirtualThreadConfig {

    /**
     * 自定义 Tomcat 使用虚拟线程处理 HTTP 请求。
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatVirtualThreadCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            log.info("Tomcat virtual threads enabled: HTTP requests will be handled by virtual threads");
        };
    }
}
