package com.alibaba.csp.sentinel.demo.dubbo.zookeeper.provider;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.demo.dubbo.zookeeper.DemoService;
import com.alibaba.dubbo.config.annotation.Service;


@Service(interfaceClass=DemoService.class,version="1.0.0")
@Component
public class DemoServiceImpl implements DemoService{

    @Override
    @SentinelResource
    public String sayHello(String name) {
        String res = new Date() + " : hello " + name;
        System.err.println(res);
        return res;
    }
    
}
