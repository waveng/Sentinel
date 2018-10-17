package com.alibaba.csp.sentinel.demo.datasource.zookeeper;

import java.io.IOException;
import java.util.List;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * Zookeeper ReadableDataSource Demo
 *
 * @author guonanjun
 */
public class ZookeeperDataSourceDashboardDemo {

    public static void main(String[] args) {
        // 使用zookeeper的场景
        loadRules2();
        try {
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 方便扩展的场景
    }


    private static void loadRules2() {

        final String remoteAddress = "127.0.0.1:2181";
        // 引入groupId和dataId的概念，是为了方便和Nacos进行切换
        final String groupId = "Sentinel-Demo";
        final String flowDataId = "SYSTEM-CODE-DEMO-FLOW";
        // final String degradeDataId = "SYSTEM-CODE-DEMO-DEGRADE";
        // final String systemDataId = "SYSTEM-CODE-DEMO-SYSTEM";


        final String nodeTypeFlow = "node-flow";
        final String nodeTypeDegrade = "node-degrade";
        final String nodeTypeSystem = "node-system";

        TransportConfig.setRuntimePort(1234);
        
        // 规则会持久化到zk的/groupId/flowDataId节点
        // groupId和和flowDataId可以用/开头也可以不用
        // 建议不用以/开头，目的是为了如果从Zookeeper切换到Nacos的话，只需要改数据源类名就可以
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, flowDataId,nodeTypeFlow,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, degradeDataId, nodeTypeDegrade,
        //         source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
        // DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
        //
        // ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, systemDataId, nodeTypeSystem,
        //         source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
        // SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

    }
}
