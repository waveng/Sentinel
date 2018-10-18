package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taobao.csp.sentinel.dashboard.client.zk.TestBase;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class ReadableDataSourceTest extends TestBase {

    @Test
    public void read() throws Exception {
        ZkClient zk = new ZkClient(remoteAddress);
        ReadableDataSource<List<FlowRuleEntity>> read = new ReadableDataSource<>(zk, NodeType.NODE_FLOW,
                new Converter<String, List<FlowRuleEntity>>() {
                    @Override
                    public List<FlowRuleEntity> convert(String source) {
                        return JSON.parseObject(source, new TypeReference<List<FlowRuleEntity>>() {
                        });
                    }

                });
        List<FlowRuleEntity> data = read.read(appname, HostNameUtil.getIp(), 1234);

        Assert.assertNotNull(data);
        Assert.assertTrue(!data.isEmpty());
        System.out.println(JSON.toJSONString(data));
    }
}
