package com.speer.assessmet.notes.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Bean
    public RateLimiter rateLimiterWithConfig() {
        // Create a custom configuration for a RateLimiter
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(60))
                .limitForPeriod(100)
                .build();

        return rateLimiterRegistry.rateLimiter("rlr", config);

    }
}
