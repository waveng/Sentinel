package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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
public class SentinelZkClientDataSourceTest extends TestBase {

    @Test
    public void test() {

        ZkClient zk = new ZkClient(remoteAddress);

        SentinelZkClientDataSource source = new SentinelZkClientDataSource();
        source.initFlow(zk);
        source.initFlow(zk);

        FlowRuleEntity fule = new FlowRuleEntity();
        fule.setApp(appname);
        fule.setStrategy(100);
        fule.setCount(10d);
        List<FlowRuleEntity> conf = new ArrayList<FlowRuleEntity>();
        conf.add(fule);

        source.setFlowRuleOfMachine(appname, HostNameUtil.getIp(), 1234, conf);

        List<FlowRuleEntity> data = source.fetchFlowRuleOfMachine(appname, HostNameUtil.getIp(), 1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println(JSON.toJSONString(data));
    }
}
