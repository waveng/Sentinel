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
 * 
 * @author waveng
 * @since 0.2.1
 * @since 0.2.1
 */
public class SentinelZkClientDataSource extends SentinelApiClientDataSource{
    private static Logger logger = LoggerFactory.getLogger(SentinelZkClientDataSource.class);
    
    private Readable<String, List<FlowRuleEntity>> readableFlowDataSource;
    private Readable<String, List<DegradeRuleEntity>> readableDegradeDataSource;
    private Readable<String, List<SystemRuleEntity>> readableSystemDataSource;
    private Readable<String, List<AuthorityRuleEntity>> readableAuthorityDataSource;
    
    private Writable<List<FlowRuleEntity>> writableFlowDataSource;
    private Writable<List<DegradeRuleEntity>> writableDegradeDataSource;
    private Writable<List<SystemRuleEntity>> writableSystemDataSource;
    
    public SentinelZkClientDataSource() {
        super();
    }

    
    

    public void setReadableFlowDataSource(Readable<String, List<FlowRuleEntity>> readableFlowDataSource) {
        this.readableFlowDataSource = readableFlowDataSource;
    }




    public void setReadableDegradeDataSource(Readable<String, List<DegradeRuleEntity>> readableDegradeDataSource) {
        this.readableDegradeDataSource = readableDegradeDataSource;
    }




    public void setReadableSystemDataSource(Readable<String, List<SystemRuleEntity>> readableSystemDataSource) {
        this.readableSystemDataSource = readableSystemDataSource;
    }




    public void setReadableAuthorityDataSource(Readable<String, List<AuthorityRuleEntity>> readableAuthorityDataSource) {
        this.readableAuthorityDataSource = readableAuthorityDataSource;
    }




    public void setWritableFlowDataSource(Writable<List<FlowRuleEntity>> writableFlowDataSource) {
        this.writableFlowDataSource = writableFlowDataSource;
    }




    public void setWritableDegradeDataSource(Writable<List<DegradeRuleEntity>> writableDegradeDataSource) {
        this.writableDegradeDataSource = writableDegradeDataSource;
    }




    public void setWritableSystemDataSource(Writable<List<SystemRuleEntity>> writableSystemDataSource) {
        this.writableSystemDataSource = writableSystemDataSource;
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
        if(readableFlowDataSource != null){
            try {
                data = readableFlowDataSource.read(app, ip, port);
            } catch (Exception e) {
                logger.warn("Reading flow rule error!", e);
            }
        }
        if(data == null || data.isEmpty()){
            data = super.fetchFlowRuleOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        List<DegradeRuleEntity> data = null;
        if(readableDegradeDataSource != null){
            try {
                data = readableDegradeDataSource.read(app, ip, port);
            } catch (Exception e) {
                logger.warn("Reading degrade rule error!", e);
            }
        }
        if(data == null || data.isEmpty()){
            data = super.fetchDegradeRuleOfMachine(app, ip, port);
        }
        return data;
    }

    @Override
    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        List<SystemRuleEntity> data = null;
        if(readableSystemDataSource != null){
            try {
                data = readableSystemDataSource.read(app, ip, port);
            } catch (Exception e) {
                logger.warn("Reading system rule error!", e);
            }
        }
        if(data == null || data.isEmpty()){
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
        if(readableAuthorityDataSource != null){
            try {
                data = readableAuthorityDataSource.read(app, ip, port);
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
        return super.setParamFlowRuleOfMachine(app, ip, port, rules);
    }
}
