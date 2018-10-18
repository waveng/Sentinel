package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelClientDataSource;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;

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
        setFlow(zk, source);
        setDegrade(zk, source);
        setSystem(zk, source);
        return  source;
    }

    private void setFlow(ZkClient zk, SentinelZkClientDataSource source) {
        ReadableDataSource<List<FlowRuleEntity>> readable = new ReadableDataSource<>(zk, NodeType.NODE_FLOW,
                new Converter<String, List<FlowRuleEntity>>() {
            @Override
            public List<FlowRuleEntity> convert(String source) {
                return JSON.parseObject(source, new TypeReference<List<FlowRuleEntity>>() {
                });
            }
            
        });
        
        WritableDataSource<List<FlowRuleEntity>> writable = new WritableDataSource<>(zk, NodeType.NODE_FLOW,
                new Converter<List<FlowRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<FlowRuleEntity> source) {
                        return JSON.toJSONBytes(source);
                    }
                    
                });
        source.setWritableFlowDataSource(writable);
        source.setReadableFlowDataSource(readable);
    }
    
    private void setDegrade(ZkClient zk, SentinelZkClientDataSource source) {
        ReadableDataSource<List<DegradeRuleEntity>> readable = new ReadableDataSource<>(zk, NodeType.NODE_DEGRADE,
                new Converter<String, List<DegradeRuleEntity>>() {
            @Override
            public List<DegradeRuleEntity> convert(String source) {
                return JSON.parseObject(source, new TypeReference<List<DegradeRuleEntity>>() {
                });
            }
            
        });
        
        WritableDataSource<List<DegradeRuleEntity>> writable = new WritableDataSource<>(zk, NodeType.NODE_DEGRADE,
                new Converter<List<DegradeRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<DegradeRuleEntity> source) {
                        return JSON.toJSONBytes(source);
                    }
                    
                });
        source.setWritableDegradeDataSource(writable);
        source.setReadableDegradeDataSource(readable);
    }
    
    private void setSystem(ZkClient zk, SentinelZkClientDataSource source) {
        ReadableDataSource<List<SystemRuleEntity>> readable = new ReadableDataSource<>(zk, NodeType.NODE_SYSTEM,
                new Converter<String, List<SystemRuleEntity>>() {
            @Override
            public List<SystemRuleEntity> convert(String source) {
                return JSON.parseObject(source, new TypeReference<List<SystemRuleEntity>>() {
                });
            }
            
        });
        
        WritableDataSource<List<SystemRuleEntity>> writable = new WritableDataSource<>(zk, NodeType.NODE_SYSTEM,
                new Converter<List<SystemRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<SystemRuleEntity> source) {
                        return JSON.toJSONBytes(source);
                    }
                    
                });
        source.setWritableSystemDataSource(writable);
        source.setReadableSystemDataSource(readable);
    }
    
}
