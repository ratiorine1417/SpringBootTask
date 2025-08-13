package com.rookies4.springboottask.runner;

import com.rookies4.springboottask.config.vo.MyEnvironment;
import com.rookies4.springboottask.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${myprop.username}")
    private String name;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties properties;

    @Autowired
    private MyEnvironment myEnvironment;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("현재 활성화된 MyEnvironment Bean = " + myEnvironment);

        logger.debug("Properties myprop.username = " + name);
        logger.debug("Properties myprop.port = " + port);

        logger.info("MyPropProperties.getName() = " + properties.getUsername());
        logger.info("MyPropProperties.getPort() = " + properties.getPort());
    }
}
