package com.bgauction.auctionservice.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientFeignConfig {

    private final String serviceInternalKey;

    public ClientFeignConfig(@Value("${service.internal-key}") String serviceInternalKey) {
        this.serviceInternalKey = serviceInternalKey;
    }

    @Bean
    public RequestInterceptor gatewayRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("X-Service-Key", serviceInternalKey);
            }
        };
    }

}
