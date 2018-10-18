package com.taobao.csp.sentinel.dashboard.client.zk.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "csp.sentinel。dashboard。zk")
public class ZookeeperProperties {
    /**
     * zk address
     */
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    

}
