/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taobao.csp.sentinel.dashboard.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.sentinel.dashboard.client.spi.DefaultSentinelApiClient;
import com.taobao.csp.sentinel.dashboard.client.spi.SentinelApiClientSpi;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.taobao.csp.sentinel.dashboard.util.RuleUtils;

/**
 * Communicate with Sentinel client.
 *
 * @author leyou
 */
@Component
public class SentinelApiClient {

    private static Logger logger = LoggerFactory.getLogger(SentinelApiClient.class);


    private SentinelApiClientSpi sentinelApiClientSpi;
    
    public SentinelApiClient() {
        ServiceLoader<SentinelApiClientSpi> loader = ServiceLoader.load(SentinelApiClientSpi.class);
        Iterator<SentinelApiClientSpi> iterator = loader.iterator();
        SentinelApiClientSpi spi = null;
        if (iterator.hasNext()) {
            spi = iterator.next();
            
        }
        
        if(spi == null){
            spi = new DefaultSentinelApiClient();   
        }
        sentinelApiClientSpi = spi;
    }

    public List<NodeVo> fetchResourceOfMachine(String ip, int port, String type) {
        return sentinelApiClientSpi.fetchResourceOfMachine(ip, port, type);
    }

    /**
     * Fetch cluster node.
     *
     * @param ip          ip to fetch
     * @param port        port of the ip
     * @param includeZero whether zero value should in the result list.
     * @return
     */
    public List<NodeVo> fetchClusterNodeOfMachine(String ip, int port, boolean includeZero) {
        return sentinelApiClientSpi.fetchClusterNodeOfMachine(ip, port, includeZero);
    }

    public List<FlowRuleEntity> fetchFlowRuleOfMachine(String app, String ip, int port) {
        return sentinelApiClientSpi.fetchFlowRuleOfMachine(app, ip, port);
    }

    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        return sentinelApiClientSpi.fetchDegradeRuleOfMachine(app, ip, port);
    }

    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        return sentinelApiClientSpi.fetchSystemRuleOfMachine(app, ip, port);
    }

    /**
     * Fetch all parameter flow rules from provided machine.
     *
     * @param app  application name
     * @param ip   machine client IP
     * @param port machine client port
     * @return all retrieved parameter flow rules
     * @since 0.2.1
     */
    public CompletableFuture<List<ParamFlowRuleEntity>> fetchParamFlowRulesOfMachine(String app, String ip, int port) {
        return sentinelApiClientSpi.fetchParamFlowRulesOfMachine(app, ip, port);
    }

    /**
     * Fetch all authority rules from provided machine.
     *
     * @param app  application name
     * @param ip   machine client IP
     * @param port machine client port
     * @return all retrieved authority rules
     * @since 0.2.1
     */
    public List<AuthorityRuleEntity> fetchAuthorityRulesOfMachine(String app, String ip, int port) {
        return sentinelApiClientSpi.fetchAuthorityRulesOfMachine(app, ip, port);
    }

    /**
     * set rules of the machine. rules == null will return immediately;
     * rules.isEmpty() means setting the rules to empty.
     *
     * @param app
     * @param ip
     * @param port
     * @param rules
     * @return whether successfully set the rules.
     */
    public boolean setFlowRuleOfMachine(String app, String ip, int port, List<FlowRuleEntity> rules) {
        return sentinelApiClientSpi.setFlowRuleOfMachine(app, ip, port, rules);
    }

    /**
     * set rules of the machine. rules == null will return immediately;
     * rules.isEmpty() means setting the rules to empty.
     *
     * @param app
     * @param ip
     * @param port
     * @param rules
     * @return whether successfully set the rules.
     */
    public boolean setDegradeRuleOfMachine(String app, String ip, int port, List<DegradeRuleEntity> rules) {
        return sentinelApiClientSpi.setDegradeRuleOfMachine(app, ip, port, rules);
    }

    /**
     * set rules of the machine. rules == null will return immediately;
     * rules.isEmpty() means setting the rules to empty.
     *
     * @param app
     * @param ip
     * @param port
     * @param rules
     * @return whether successfully set the rules.
     */
    public boolean setSystemRuleOfMachine(String app, String ip, int port, List<SystemRuleEntity> rules) {
        return sentinelApiClientSpi.setSystemRuleOfMachine(app, ip, port, rules);
    }

    public CompletableFuture<Void> setParamFlowRuleOfMachine(String app, String ip, int port, List<ParamFlowRuleEntity> rules) {
        return sentinelApiClientSpi.setParamFlowRuleOfMachine(app, ip, port, rules);
    }

   
}
