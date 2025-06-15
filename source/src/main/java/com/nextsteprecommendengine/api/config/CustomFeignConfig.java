package com.nextsteprecommendengine.api.config;

import feign.Contract;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Feign.class})
@Slf4j
public class CustomFeignConfig {

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }
}
