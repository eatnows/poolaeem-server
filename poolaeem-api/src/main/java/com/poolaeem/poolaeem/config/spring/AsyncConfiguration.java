package com.poolaeem.poolaeem.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration {
    @Bean("asyncThreadPoolTaskExecutor")
    public TaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(30);
        taskExecutor.setThreadNamePrefix("Async-");
        taskExecutor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutor);
    }
}
