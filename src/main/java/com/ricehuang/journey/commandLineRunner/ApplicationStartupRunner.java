package com.ricehuang.journey.commandLineRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
public class ApplicationStartupRunner implements CommandLineRunner {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void run(String... args) throws Exception {
        logger.info("ApplicationStartupRunner implementing CommandLineRunner with @Order(value = 2) run method Started !!");
    }

}