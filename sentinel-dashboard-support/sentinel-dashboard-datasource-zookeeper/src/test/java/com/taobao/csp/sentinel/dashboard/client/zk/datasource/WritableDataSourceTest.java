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
import com.taobao.csp.sentinel.dashboard.client.zk.TestBase;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class WritableDataSourceTest extends TestBase{


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
        fule.setApp(appname);
        fule.setStrategy(100);
        fule.setCount(10d);
        List<FlowRuleEntity> conf = new ArrayList<FlowRuleEntity>();
        conf.add(fule);
        
        Writable.write(appname, HostNameUtil.getIp(), 1234, conf);
                
    }

}
