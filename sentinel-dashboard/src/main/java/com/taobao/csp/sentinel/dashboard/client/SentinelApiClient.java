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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelClientDataSource;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;

/**
 * Communicate with Sentinel client.
 *
 * @author leyou
 */
@Component
public class SentinelApiClient {


    @Autowired
    private SentinelClientDataSource sentinelClientDataSource;
    

    public List<NodeVo> fetchResourceOfMachine(String ip, int port, String type) {
        return sentinelClientDataSource.fetchResourceOfMachine(ip, port, type);
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
        return sentinelClientDataSource.fetchClusterNodeOfMachine(ip, port, includeZero);
    }

    public List<FlowRuleEntity> fetchFlowRuleOfMachine(String app, String ip, int port) {
        return sentinelClientDataSource.fetchFlowRuleOfMachine(app, ip, port);
    }

    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        return sentinelClientDataSource.fetchDegradeRuleOfMachine(app, ip, port);
    }

    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        
        return sentinelClientDataSource.fetchSystemRuleOfMachine(app, ip, port);
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
        try {
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
        } catch (Exception e) {
            return newFailedFuture(e);
        }
        return sentinelClientDataSource.fetchParamFlowRulesOfMachine(app, ip, port);
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
        AssertUtil.notEmpty(app, "Bad app name");
        AssertUtil.notEmpty(ip, "Bad machine IP");
        AssertUtil.isTrue(port > 0, "Bad machine port");
        return sentinelClientDataSource.fetchAuthorityRulesOfMachine(app, ip, port);
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
        if (rules == null) {
            return true;
        }
        if (ip == null) {
            throw new IllegalArgumentException("ip is null");
        }
        return sentinelClientDataSource.setFlowRuleOfMachine(app, ip, port, rules);
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
        if (rules == null) {
            return true;
        }
        if (ip == null) {
            throw new IllegalArgumentException("ip is null");
        }
        return sentinelClientDataSource.setDegradeRuleOfMachine(app, ip, port, rules);
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
        if (rules == null) {
            return true;
        }
        if (ip == null) {
            throw new IllegalArgumentException("ip is null");
        }
        return sentinelClientDataSource.setSystemRuleOfMachine(app, ip, port, rules);
    }

    public boolean setAuthorityRuleOfMachine(String app, String ip, int port, List<AuthorityRuleEntity> rules) {
        if (rules == null) {
            return true;
        }
        if (StringUtil.isBlank(ip) || port <= 0) {
            throw new IllegalArgumentException("Invalid IP or port");
        }
        return sentinelClientDataSource.setAuthorityRuleOfMachine(app, ip, port, rules);
    }

    public CompletableFuture<Void> setParamFlowRuleOfMachine(String app, String ip, int port, List<ParamFlowRuleEntity> rules) {
        if (rules == null) {
            return CompletableFuture.completedFuture(null);
        }
        if (StringUtil.isBlank(ip) || port <= 0) {
            return newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        return sentinelClientDataSource.setParamFlowRuleOfMachine(app, ip, port, rules);
    }

    protected <R> CompletableFuture<R> newFailedFuture(Throwable ex) {
        CompletableFuture<R> future = new CompletableFuture<>();
        future.completeExceptionally(ex);
        return future;
    }
}
