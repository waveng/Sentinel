package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class ReadableDataSourceTest {

    private final String remoteAddress = "127.0.0.1:2181";

    @Before
    public void init() {
        final String groupId = "Sentinel-Demo";
        final String flowDataId = "SYSTEM-CODE-DEMO-FLOW";

        final String nodeTypeFlow = "node-flow";

        TransportConfig.setRuntimePort(1234);

     // 规则会持久化到zk的/groupId/flowDataId节点
        // groupId和和flowDataId可以用/开头也可以不用
        // 建议不用以/开头，目的是为了如果从Zookeeper切换到Nacos的话，只需要改数据源类名就可以
        ZookeeperDataSource<List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, flowDataId, nodeTypeFlow, new Converter<String, List<FlowRule>>() {
                    @Override
                    public List<FlowRule> convert(String source) {
                        return null;
                    }
                });
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }

    @Test
    public void read() throws Exception {
        final String nodeTypeFlow = "node-flow";
        ZkClient zk = new ZkClient(remoteAddress);
        ReadableDataSource<List<FlowRuleEntity>> read = new ReadableDataSource<>(zk, nodeTypeFlow,
                new Converter<String, List<FlowRuleEntity>>() {
                    @Override
                    public List<FlowRuleEntity> convert(String source) {
                        return JSON.parseObject(source, new TypeReference<List<FlowRuleEntity>>() {
                        });
                    }

                });
        List<FlowRuleEntity> data = read.read(
                "com.alibaba.csp.sentinel.demo.datasource.zookeeper.ZookeeperDataSourceDashboardDemo",
                HostNameUtil.getIp(), 1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println(JSON.toJSONString(data));
    }
}
