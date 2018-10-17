package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.taobao.csp.sentinel.dashboard.client.datasource.SentinelApiClientDataSource;
import com.taobao.csp.sentinel.dashboard.client.zk.Writable;
import com.taobao.csp.sentinel.dashboard.client.zk.Readable;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.taobao.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;

/**
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class ZkSentinelApiClientDataSource extends SentinelApiClientDataSource{
    private static Logger logger = LoggerFactory.getLogger(ZkSentinelApiClientDataSource.class);
    
    private Readable<String, List<FlowRuleEntity>> readableFlow;
    private Readable<String, List<DegradeRuleEntity>> readableDegrade;
    private Readable<String, List<SystemRuleEntity>> readableSystem;
    private Readable<String, List<AuthorityRuleEntity>> readableAuthority;
    
    private Writable<List<FlowRuleEntity>> writableFlow;
    private Writable<List<DegradeRuleEntity>> writableDegrade;
    private Writable<List<SystemRuleEntity>> writableSystem;
    
    public ZkSentinelApiClientDataSource() {
        super();
    }

    
    public void setReadableFlow(Readable<String, List<FlowRuleEntity>> readableFlow) {
        this.readableFlow = readableFlow;
    }


    public void setReadableDegrade(Readable<String, List<DegradeRuleEntity>> readableDegrade) {
        this.readableDegrade = readableDegrade;
    }


    public void setReadableSystem(Readable<String, List<SystemRuleEntity>> readableSystem) {
        this.readableSystem = readableSystem;
    }


    public void setReadableAuthority(Readable<String, List<AuthorityRuleEntity>> readableAuthority) {
        this.readableAuthority = readableAuthority;
    }


    public void setWritableFlow(Writable<List<FlowRuleEntity>> writableFlow) {
        this.writableFlow = writableFlow;
    }



    public void setWritableDegrade(Writable<List<DegradeRuleEntity>> writableDegrade) {
        this.writableDegrade = writableDegrade;
    }



    public void setWritableSystem(Writable<List<SystemRuleEntity>> writableSystem) {
        this.writableSystem = writableSystem;
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
        List<FlowRuleEntity> data = null;
        if(readableFlow != null){
            try {
                data = readableFlow.read(app, ip, port);
            } catch (Exception e) {
                logger.error("readable flow error !", e);
            }
        }
        if(data == null){
            data = super.fetchFlowRuleOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        List<DegradeRuleEntity> data = null;
        if(readableDegrade != null){
            try {
                data = readableDegrade.read(app, ip, port);
            } catch (Exception e) {
                logger.error("readable degrade error !", e);
            }
        }
        if(data == null){
            data = super.fetchDegradeRuleOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        List<SystemRuleEntity> data = null;
        if(readableSystem != null){
            try {
                data = readableSystem.read(app, ip, port);
            } catch (Exception e) {
                logger.error("readable degrade error !", e);
            }
        }
        if(data == null){
            data = super.fetchSystemRuleOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public CompletableFuture<List<ParamFlowRuleEntity>> fetchParamFlowRulesOfMachine(String app, String ip, int port) {
        
        return super.fetchParamFlowRulesOfMachine(app, ip, port);
    }

    @Override
    public List<AuthorityRuleEntity> fetchAuthorityRulesOfMachine(String app, String ip, int port) {
        List<AuthorityRuleEntity> data = null;
        if(readableAuthority != null){
            try {
                data = readableAuthority.read(app, ip, port);
            } catch (Exception e) {
                logger.error("readable degrade error !", e);
            }
        }
        if(data == null){
            data = super.fetchAuthorityRulesOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public boolean setFlowRuleOfMachine(String app, String ip, int port, List<FlowRuleEntity> rules) {
        if(writableFlow == null){
            return super.setFlowRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableFlow.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.error("write flow error!", e);
        }
        return false;
    }

    @Override
    public boolean setDegradeRuleOfMachine(String app, String ip, int port, List<DegradeRuleEntity> rules) {
        if(writableDegrade == null){
            return super.setDegradeRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableDegrade.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.error("write degradee error!", e);
        }
        return false;
    }

    @Override
    public boolean setSystemRuleOfMachine(String app, String ip, int port, List<SystemRuleEntity> rules) {
        if(writableSystem == null){
            return super.setSystemRuleOfMachine(app, ip, port, rules);
        }
        try {
            writableSystem.write(app, ip, port, rules);
            return true;
        } catch (Exception e) {
            logger.error("write degradee error!", e);
        }
        return false;
    }

    @Override
    public CompletableFuture<Void> setParamFlowRuleOfMachine(String app, String ip, int port, List<ParamFlowRuleEntity> rules) {
        return super.setParamFlowRuleOfMachine(app, ip, port, rules);
    }
}
