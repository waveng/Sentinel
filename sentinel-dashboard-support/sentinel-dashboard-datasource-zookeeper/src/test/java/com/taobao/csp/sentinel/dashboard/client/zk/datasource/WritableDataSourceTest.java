package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class WritableDataSourceTest{

     
     private final String remoteAddress = "127.0.0.1:2181";

    @Before
    public void init() {
        final String groupId = "Sentinel-Demo";
        final String flowDataId = "SYSTEM-CODE-DEMO-FLOW";


        TransportConfig.setRuntimePort(1234);

     // 规则会持久化到zk的/groupId/flowDataId节点
        // groupId和和flowDataId可以用/开头也可以不用
        // 建议不用以/开头，目的是为了如果从Zookeeper切换到Nacos的话，只需要改数据源类名就可以
        ZookeeperDataSource<List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, flowDataId, NodeType.NODE_FLOW, new Converter<String, List<FlowRule>>() {
                    @Override
                    public List<FlowRule> convert(String source) {
                        return null;
                    }
                });
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }

     @Test
     public void read() throws Exception{
        ZkClient zk = new ZkClient(remoteAddress);
        WritableDataSource<List<FlowRuleEntity>> Writable = new WritableDataSource<>(zk, NodeType.NODE_FLOW, new Converter<List<FlowRuleEntity>, byte[]>() {

        @Override
        public byte[] convert(List<FlowRuleEntity> source) {
            return JSON.toJSONBytes(source);
        }
         
     });
        FlowRuleEntity fule = new FlowRuleEntity();
        fule.setApp("com.alibaba.csp.sentinel.demo.datasource.zookeeper.ZookeeperDataSourceDashboardDemo");
        fule.setStrategy(100);
        fule.setCount(10d);
        List<FlowRuleEntity> conf = new ArrayList<FlowRuleEntity>();
        conf.add(fule);
        
        Writable.write("com.alibaba.csp.sentinel.demo.datasource.zookeeper.ZookeeperDataSourceDashboardDemo", HostNameUtil.getIp(), 1234, conf);
                
    }

}
