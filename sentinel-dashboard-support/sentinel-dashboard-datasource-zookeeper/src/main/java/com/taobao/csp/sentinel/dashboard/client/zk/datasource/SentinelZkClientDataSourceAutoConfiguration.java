package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelClientDataSource;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class SentinelZkClientDataSourceAutoConfiguration {
    
    
    @Bean
    public SentinelClientDataSource sentinelClientDataSource(ZookeeperProperties zookeeperProperties) {
        
        ZkClient zk = new ZkClient(zookeeperProperties.getAddress());
        SentinelZkClientDataSource source = new SentinelZkClientDataSource();
        source.initFlow(zk);
        source.initDegrade(zk );
        source.initSystem(zk );
        source.initParamFlow(zk);
        return  source;
    }

}
