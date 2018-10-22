package com.taobao.csp.sentinel.dashboard.client.datasource;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.taobao.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import com.taobao.csp.sentinel.dashboard.repository.metric.InMemoryMetricsRepository;
import com.taobao.csp.sentinel.dashboard.repository.metric.MetricsRepository;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@Configuration
public class SentinelClientDataSourceAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean(SentinelClientDataSource.class)
    public SentinelClientDataSource sentinelClientDataSource() {
        return new SentinelApiClientDataSource();   
    }
    
    @Bean
    @ConditionalOnMissingBean(MetricsRepository.class)
    public MetricsRepository<MetricEntity> metricsRepository(){
        return new InMemoryMetricsRepository();
    }
}
