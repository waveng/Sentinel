package com.alibaba.csp.sentinel.demo.dubbo.zookeeper.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

@SpringBootApplication
@EnableDubboConfiguration
@RestController
public class ConsumerMain {

    @Autowired
    private DemoServiceImpl demoServiceImpl;

    public static void main(String[] args) {
        System.setProperty("project.name", "dubbo-sentine-zookeeper-consumer");
        System.setProperty("csp.sentinel.dashboard.server", "127.0.0.1:8080");
        System.setProperty("csp.sentinel.api.port", "7871");
        SpringApplication.run(ConsumerMain.class, args);

    }

    @RequestMapping("/get")
    public void get() {
        System.err.println(demoServiceImpl.sayHello("王波"));
    }

    @RequestMapping("/get/{size}")
    public void get(@PathVariable int size) {
        for (int i = 0; i < size; i++) {
            System.err.println(demoServiceImpl.sayHello("王波"));
        }

    }

}
