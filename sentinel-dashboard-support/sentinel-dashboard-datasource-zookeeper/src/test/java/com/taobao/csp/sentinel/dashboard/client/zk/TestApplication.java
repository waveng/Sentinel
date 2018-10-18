package com.taobao.csp.sentinel.dashboard.client.zk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        TestBase.setPropertyProjectName();
        SpringApplication.run(TestApplication.class, args);
    }
}
