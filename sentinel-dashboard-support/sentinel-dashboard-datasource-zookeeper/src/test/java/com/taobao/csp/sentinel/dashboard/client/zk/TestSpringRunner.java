package com.taobao.csp.sentinel.dashboard.client.zk;

import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class TestSpringRunner extends SpringJUnit4ClassRunner {
    static{
        TestBase.setPropertyProjectName();
    }
    public TestSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

}
