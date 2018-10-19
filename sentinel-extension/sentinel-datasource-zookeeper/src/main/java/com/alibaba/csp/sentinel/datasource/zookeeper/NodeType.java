package com.alibaba.csp.sentinel.datasource.zookeeper;

public enum NodeType {
    NODE_FLOW,
    NODE_DEGRADE,
    NODE_SYSTEM,
    NODE_AUTHORITY,
    NODE_PARAM_FLOW;
    public String toString(){
        return this.name().toLowerCase();
    }
}
