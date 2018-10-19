package com.alibaba.csp.sentinel.demo.dubbo.zookeeper.consumer;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.demo.dubbo.zookeeper.DemoService;
import com.alibaba.dubbo.config.annotation.Reference;

@Service
public class DemoServiceImpl{

    @Reference(version="1.0.0", timeout = 30000)
    private DemoService demoService;
    
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }
    
    public String error(){  
        return "error";
        
    }
}
