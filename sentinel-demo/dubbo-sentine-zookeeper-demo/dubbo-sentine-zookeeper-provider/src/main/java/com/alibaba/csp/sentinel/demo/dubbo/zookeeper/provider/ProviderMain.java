package com.alibaba.csp.sentinel.demo.dubbo.zookeeper.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

@SpringBootApplication
@EnableDubboConfiguration
public class ProviderMain { 
    public static void main(String[] args) {
        System.setProperty("project.name", "dubbo-sentine-zookeeper-provider");
        System.setProperty("csp.sentinel.dashboard.server", "127.0.0.1:8080");
        System.setProperty("csp.sentinel.api.port", "7870");
        
        SpringApplication.run(ProviderMain.class);
    }

}
