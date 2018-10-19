package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelClientDataSource;
import com.taobao.csp.sentinel.dashboard.client.zk.TestApplication;
import com.taobao.csp.sentinel.dashboard.client.zk.TestBase;
import com.taobao.csp.sentinel.dashboard.client.zk.TestSpringRunner;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;

@RunWith(TestSpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class SentinelZkClientDataSourceAutoConfigurationTest extends TestBase {

    @Autowired
    SentinelClientDataSource sentinelClientDataSource;

    @Test
    public void setFlowRuleOfMachine() {

        FlowRuleEntity fule = new FlowRuleEntity();
        fule.setApp(appname);
        fule.setStrategy(100);
        fule.setCount(10d);
        fule.setIp(HostNameUtil.getIp());
        fule.setGrade(10);
        fule.setGmtCreate(new Date());
        List<FlowRuleEntity> conf = new ArrayList<FlowRuleEntity>();
        conf.add(fule);

        sentinelClientDataSource.setFlowRuleOfMachine(appname, HostNameUtil.getIp(), 1234, conf);

        List<FlowRuleEntity> data = sentinelClientDataSource.fetchFlowRuleOfMachine(appname, HostNameUtil.getIp(),
                1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println("----------------------" + JSON.toJSONString(data));
    }
    
    @Test
    public void setDegradeRuleOfMachine() {

        DegradeRuleEntity fule = new DegradeRuleEntity();
        fule.setApp(appname);
        fule.setCount(10d);
        fule.setIp(HostNameUtil.getIp());
        fule.setGrade(10);
        fule.setGmtCreate(new Date());
        fule.setTimeWindow(12132);
        List<DegradeRuleEntity> conf = new ArrayList<DegradeRuleEntity>();
        conf.add(fule);

        sentinelClientDataSource.setDegradeRuleOfMachine(appname, HostNameUtil.getIp(), 1234, conf);

        List<DegradeRuleEntity> data = sentinelClientDataSource.fetchDegradeRuleOfMachine(appname, HostNameUtil.getIp(),
                1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println("----------------------" + JSON.toJSONString(data));
    }
    
    
    @Test
    public void setSystemRuleOfMachine() {

        SystemRuleEntity fule = new SystemRuleEntity();
        fule.setApp(appname);
        fule.setQps(100D);
        fule.setIp(HostNameUtil.getIp());
        fule.setAvgLoad(123234343d);
        fule.setAvgRt(12l);
        fule.setMaxThread(12l);
        List<SystemRuleEntity> conf = new ArrayList<SystemRuleEntity>();
        conf.add(fule);

        sentinelClientDataSource.setSystemRuleOfMachine(appname, HostNameUtil.getIp(), 1234, conf);

        List<SystemRuleEntity> data = sentinelClientDataSource.fetchSystemRuleOfMachine(appname, HostNameUtil.getIp(),
                1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println("----------------------" + JSON.toJSONString(data));
    }
    
    @Test
    public void setParamFlowRuleOfMachine() {

        ParamFlowRuleEntity fule = new ParamFlowRuleEntity();
        fule.setApp(appname);
        fule.setIp(HostNameUtil.getIp());
        fule.setId(12324354656L);
        List<ParamFlowRuleEntity> conf = new ArrayList<ParamFlowRuleEntity>();
        conf.add(fule);

        sentinelClientDataSource.setParamFlowRuleOfMachine(appname, HostNameUtil.getIp(), 1234, conf);

        List<SystemRuleEntity> data = sentinelClientDataSource.fetchSystemRuleOfMachine(appname, HostNameUtil.getIp(),
                1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println("----------------------" + JSON.toJSONString(data));
    }
}
