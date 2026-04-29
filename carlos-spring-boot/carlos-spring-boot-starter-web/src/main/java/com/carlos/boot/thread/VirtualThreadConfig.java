package com.carlos.boot.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 虚拟线程（Virtual Threads）全局配置。
 *
 * <p>Java 21 正式支持虚拟线程（JEP 444），该配置类为框架提供统一的虚拟线程执行器，
 * 适用于 I/O 密集型异步任务（Redis 批量操作、消息发送、Feign 调用、数据库批处理等）。</p>
 *
 * <p><b>注意</b>：虚拟线程不适合 CPU 密集型任务或持有平台线程锁（synchronized、ReentrantLock）
 * 的长时间运行任务，此类场景仍应使用平台线程池。</p>
 *
 * @author carlos
 * @since 3.0.0
 */
@Configuration
public class VirtualThreadConfig implements AsyncConfigurer {

    /**
     * 创建虚拟线程执行器，用于 I/O 密集型异步任务。
     *
     * <p>每个任务都会在一个全新的虚拟线程上执行，虚拟线程由 JVM 调度到少量的平台线程上运行，
     * 可轻松支撑数十万级并发。</p>
     */
    @Bean(name = {"virtualTaskExecutor", "taskExecutor"})
    public ExecutorService virtualTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 配置 Spring @Async 默认使用虚拟线程执行器。
     *
     * <p>所有未显式指定线程池名称的 {@code @Async} 方法都会使用该执行器，
     * 避免回退到 {@code SimpleAsyncTaskExecutor}（每任务创建新平台线程，性能极差）。</p>
     */
    @Override
    public Executor getAsyncExecutor() {
        return virtualTaskExecutor();
    }
}
