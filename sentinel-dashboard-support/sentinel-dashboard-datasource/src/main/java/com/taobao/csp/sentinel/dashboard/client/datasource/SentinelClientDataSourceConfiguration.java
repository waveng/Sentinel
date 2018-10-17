package com.taobao.csp.sentinel.dashboard.client.datasource;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@Configuration
public class SentinelClientDataSourceConfiguration {
    
    @Bean
    @ConditionalOnMissingBean(SentinelClientDataSource.class)
    public SentinelClientDataSource sentinelClientDataSource() {
        return new SentinelApiClientDataSource();   
    }
}
