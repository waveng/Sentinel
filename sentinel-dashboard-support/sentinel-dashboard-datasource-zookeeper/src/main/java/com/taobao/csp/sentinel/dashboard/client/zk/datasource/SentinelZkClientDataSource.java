package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.NodeType;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelApiClientDataSource;
import com.taobao.csp.sentinel.dashboard.client.zk.Readable;
import com.taobao.csp.sentinel.dashboard.client.zk.Writable;
import com.taobao.csp.sentinel.dashboard.client.zk.ZkClient;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.taobao.csp.sentinel.dashboard.util.RuleUtils;


/**
 * 
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class SentinelZkClientDataSource extends SentinelApiClientDataSource{
    private static Logger logger = LoggerFactory.getLogger(SentinelZkClientDataSource.class);
    
    private Readable<String, List<FlowRule>> readableFlowDataSource;
    private Readable<String, List<DegradeRule>> readableDegradeDataSource;
    private Readable<String, List<SystemRule>> readableSystemDataSource;
    private Readable<String, List<AuthorityRule>> readableAuthorityDataSource;
    private Readable<String, List<ParamFlowRule>> readableParamFlowDataSource;
    
    private Writable<List<FlowRuleEntity>> writableFlowDataSource;
    private Writable<List<DegradeRuleEntity>> writableDegradeDataSource;
    private Writable<List<SystemRuleEntity>> writableSystemDataSource;
    private Writable<List<ParamFlowRuleEntity>> writableParamFlowDataSource;
    
    public SentinelZkClientDataSource() {
        super();
    }

    
    

    public void initFlow(ZkClient zk) {
        this.readableFlowDataSource = new ReadableDataSource<>(zk, NodeType.NODE_FLOW,
                new Converter<String, List<FlowRule>>() {
            @Override
            public List<FlowRule> convert(String source) {
                return RuleUtils.parseFlowRule(source);
            }
            
        });
        
        this.writableFlowDataSource = new WritableDataSource<>(zk, NodeType.NODE_FLOW,
                new Converter<List<FlowRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<FlowRuleEntity> source) {
                        return JSON.toJSONBytes(source.stream().map(FlowRuleEntity::toFlowRule).collect(Collectors.toList()));
                    }
                    
                });;
    }




    public void initDegrade(ZkClient zk) {
        this.readableDegradeDataSource = new ReadableDataSource<>(zk, NodeType.NODE_DEGRADE,
                new Converter<String, List<DegradeRule>>() {
            @Override
            public List<DegradeRule> convert(String source) {
                return RuleUtils.parseDegradeRule(source);
            }
            
        });
        this.writableDegradeDataSource = new WritableDataSource<>(zk, NodeType.NODE_DEGRADE,
                new Converter<List<DegradeRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<DegradeRuleEntity> source) {
                        return JSON.toJSONBytes(source.stream().map(DegradeRuleEntity::toDegradeRule).collect(Collectors.toList()));
                    }
                    
                });
        
    }




    public void initSystem(ZkClient zk) {
        this.readableSystemDataSource = new ReadableDataSource<>(zk, NodeType.NODE_SYSTEM,
                new Converter<String, List<SystemRule>>() {
            @Override
            public List<SystemRule> convert(String source) {
                return RuleUtils.parseSystemRule(source);
            }
            
        });
        
        this.writableSystemDataSource =  new WritableDataSource<>(zk, NodeType.NODE_SYSTEM,
                new Converter<List<SystemRuleEntity>, byte[]>() {
                    
                    @Override
                    public byte[] convert(List<SystemRuleEntity> source) {
                        return JSON.toJSONBytes(source.stream().map(SystemRuleEntity::toSystemRule).collect(Collectors.toList()));
                    }
                    
                });
    }




    public void initAuthority(ZkClient zk) {
        this.readableAuthorityDataSource  = new ReadableDataSource<>(zk, NodeType.NODE_AUTHORITY,
                new Converter<String, List<AuthorityRule>>() {
            @Override
            public List<AuthorityRule> convert(String source) {
                return RuleUtils.parseAuthorityRule(source);
            }
            
        });
    }
    
    public void initParamFlow(ZkClient zk) {
        this.readableParamFlowDataSource  = new ReadableDataSource<>(zk, NodeType.NODE_PARAM_FLOW,
                new Converter<String, List<ParamFlowRule>>() {
            @Override
            public List<ParamFlowRule> convert(String source) {
                return RuleUtils.parseParamFlowRule(source);
            }
            
        });
        
        this.writableParamFlowDataSource =  new WritableDataSource<>(zk, NodeType.NODE_PARAM_FLOW,
                new Converter<List<ParamFlowRuleEntity>, byte[]>() {
                        
                    @Override
                    public byte[] convert(List<ParamFlowRuleEntity> source) {
                        return JSON.toJSONBytes(source.stream().map(ParamFlowRuleEntity::getRule).collect(Collectors.toList()));
                    }
                    
                });
    }


    @Override
    public List<NodeVo> fetchResourceOfMachine(String ip, int port, String type) {
        return super.fetchResourceOfMachine(ip, port, type);
    }

    @Override
    public List<NodeVo> fetchClusterNodeOfMachine(String ip, int port, boolean includeZero) {
        return super.fetchClusterNodeOfMachine(ip, port, includeZero);
    }


    @Override
    public List<FlowRuleEntity> fetchFlowRuleOfMachine(String app, String ip, int port) {
        if(readableFlowDataSource != null){
            try {
                List<FlowRule> rules = readableFlowDataSource.read(app, ip, port);
                if(!(rules == null || rules.isEmpty())){
                    return rules.stream().map(rule -> FlowRuleEntity.fromFlowRule(app, ip, port, rule))
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                logger.warn("Reading flow rule error!", e);
            }
        }
        return super.fetchFlowRuleOfMachine(app, ip, port);
    }

    @Override
    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        if(readableDegradeDataSource != null){
            try {
                List<DegradeRule> rules =  readableDegradeDataSource.read(app, ip, port);
                if(!(rules == null || rules.isEmpty())){
                    return rules.stream().map(rule -> DegradeRuleEntity.fromDegradeRule(app, ip, port, rule))
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                logger.warn("Reading degrade rule error!", e);
            }
        }
        return super.fetchDegradeRuleOfMachine(app, ip, port);
    }

    @Override
    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        if(readableSystemDataSource != null){
            try {
                List<SystemRule> rules = readableSystemDataSource.read(app, ip, port);
                if(!(rules == null || rules.isEmpty())){
                    return rules.stream().map(rule -> SystemRuleEntity.fromSystemRule(app, ip, port, rule))
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                logger.warn("Reading system rule error!", e);
            }
        }
        return super.fetchSystemRuleOfMachine(app, ip, port);
    }

    @Override
    public CompletableFuture<List<ParamFlowRuleEntity>> fetchParamFlowRulesOfMachine(String app, String ip, int port) {
        CompletableFuture<List<ParamFlowRuleEntity>> future = null;
        if(readableParamFlowDataSource != null){
            try {
                List<ParamFlowRule> rules = readableParamFlowDataSource.read(app, ip, port);
                if(!(rules == null || rules.isEmpty())){
                    future = new CompletableFuture<>();
                    future.complete(rules.stream()
                            .map(e -> ParamFlowRuleEntity.fromAuthorityRule(app, ip, port, e))
                            .collect(Collectors.toList()));
                    
                    return future;
                }
                
            } catch (Exception e) {
                logger.warn("Reading param flow rule error!", e);
            }
            
        }
        return super.fetchParamFlowRulesOfMachine(app, ip, port);
    }

    @Override
    public List<AuthorityRuleEntity> fetchAuthorityRulesOfMachine(String app, String ip, int port) {
        List<AuthorityRuleEntity> data = null;
        if(readableAuthorityDataSource != null){
            try {
                List<AuthorityRule> rules = readableAuthorityDataSource.read(app, ip, port);
                if(!(rules == null || rules.isEmpty())){
                    return rules.stream().map(rule -> AuthorityRuleEntity.fromAuthorityRule(app, ip, port, rule))
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                logger.warn("Reading authority rule error!", e);
            }
        }
        if(data == null || data.isEmpty()){
            data = super.fetchAuthorityRulesOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public boolean setFlowRuleOfMachine(String app, String ip, int port, List<FlowRuleEntity> rules) {
        if(writableFlowDataSource == null){
            return super.setFlowRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableFlowDataSource.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.warn("Write flow rule error!", e);
        }
        return false;
    }

    @Override
    public boolean setDegradeRuleOfMachine(String app, String ip, int port, List<DegradeRuleEntity> rules) {
        if(writableDegradeDataSource == null){
            return super.setDegradeRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableDegradeDataSource.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.warn("Write degradee rule error!", e);
        }
        return false;
    }

    @Override
    public boolean setSystemRuleOfMachine(String app, String ip, int port, List<SystemRuleEntity> rules) {
        if(writableSystemDataSource == null){
            return super.setSystemRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableSystemDataSource.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.warn("Write system rule error!", e);
        }
        return false;
    }

    @Override
    public CompletableFuture<Void> setParamFlowRuleOfMachine(String app, String ip, int port, List<ParamFlowRuleEntity> rules) {
        if(writableParamFlowDataSource == null){
            return super.setParamFlowRuleOfMachine(app, ip, port, rules);
        }
        
        try {
            writableParamFlowDataSource.write(app, ip, port, rules);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.warn("Error when setting parameter flow rule", e);
            return newFailedFuture(e);
        }
    }
}
