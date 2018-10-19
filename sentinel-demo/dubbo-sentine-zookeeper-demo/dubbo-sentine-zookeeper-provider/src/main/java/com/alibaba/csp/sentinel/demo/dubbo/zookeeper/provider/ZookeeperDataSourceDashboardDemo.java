package com.alibaba.csp.sentinel.demo.dubbo.zookeeper.provider;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * Zookeeper ReadableDataSource Demo， sentinel-dashboard-datasource-zookeeper 支持
 *
 * @author guonanjun
 */
@Configuration
public class ZookeeperDataSourceDashboardDemo {


    @PostConstruct
    private void init() {
        final String remoteAddress = "127.0.0.1:2181";
        final String groupId = "dubbo-sentine-zookeeper-provider";
        final String flowDataId = "SYSTEM-CODE-DEMO-FLOW";
        final String degradeDataId = "SYSTEM-CODE-DEMO-DEGRADE";
        final String systemDataId = "SYSTEM-CODE-DEMO-SYSTEM";

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, flowDataId, NodeType.NODE_FLOW,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, degradeDataId, NodeType.NODE_DEGRADE,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                }));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, systemDataId, NodeType.NODE_SYSTEM,
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
                }));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

    }
}
