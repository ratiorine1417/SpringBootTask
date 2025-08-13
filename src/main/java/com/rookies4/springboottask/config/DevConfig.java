package com.rookies4.springboottask.config;

import com.rookies4.springboottask.config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class DevConfig {
    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("개발환경")
                .build();
    }
}