package com.github.mmore.async.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TraceConfig {

    @Bean
    public ThreadPoolTaskExecutor executorService() {
        ThreadPoolTaskExecutor executorService = new ThreadPoolTaskExecutor();
        executorService.setCorePoolSize(1);
        executorService.setMaxPoolSize(1);
        executorService.setThreadNamePrefix("AsyncTask-");
        executorService.setAwaitTerminationSeconds(60);
        executorService.setWaitForTasksToCompleteOnShutdown(true);
        executorService.initialize();
        return executorService;
    }
}
