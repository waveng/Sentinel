package com.taobao.csp.sentinel.dashboard.client.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class TestBase {
    protected final String remoteAddress = "127.0.0.1:2181";
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;
    protected final static String appname = "Sentinel-Demo";

    public static void setPropertyProjectName(){
        System.setProperty("project.name", appname);
    }
    @Before
    public void init() throws Exception {
        final String groupId = "Sentinel-Demo";
        final String flowDataId = "SYSTEM-CODE-DEMO-FLOW";
        final String degradeDataId = "SYSTEM-CODE-DEMO-DEGRADE";
        final String systemDataId = "SYSTEM-CODE-DEMO-SYSTEM";

        TransportConfig.setRuntimePort(1234);

        ZookeeperDataSource<List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress,
                groupId, flowDataId, NodeType.NODE_FLOW, new Converter<String, List<FlowRule>>() {
                    @Override
                    public List<FlowRule> convert(String source) {
                        return null;
                    }
                });
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
        
        ZookeeperDataSource<List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, degradeDataId, NodeType.NODE_DEGRADE,
                 source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
         DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
        
         ZookeeperDataSource<List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, systemDataId, NodeType.NODE_SYSTEM,
                 source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
         SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
        
       
        final String rule = "[\n"
                + "  {\n"
                + "    \"resource\": \"TestResource\",\n"
                + "    \"controlBehavior\": 0,\n"
                + "    \"count\": 10.0,\n"
                + "    \"grade\": 1,\n"
                + "    \"limitApp\": \"default\",\n"
                + "    \"strategy\": 0\n"
                + "  }\n"
                + "]";

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress, new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        zkClient.start();
        String path = getPath(groupId, flowDataId);
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, rule.getBytes());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zkClient.close();
    }

    private static String getPath(String groupId, String dataId) {
        String path = "";
        if (groupId.startsWith("/")) {
            path += groupId;
        } else {
            path += "/" + groupId;
        }
        if (dataId.startsWith("/")) {
            path += dataId;
        } else {
            path += "/" + dataId;
        }
        return path;
    }
}
