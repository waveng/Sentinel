package com.taobao.csp.sentinel.dashboard.client.spi;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 999)
@Configuration
public class SentinelClientDataSourceConfiguration {
    
    @Bean
    @ConditionalOnMissingBean(SentinelClientDataSource.class)
    public SentinelClientDataSource sentinelClientDataSource() {
        return new SentinelApiClientDataSource();   
    }
}
