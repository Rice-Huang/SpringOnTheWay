package com.ricehuang.journey.commandLineRunner;

import com.ricehuang.journey.consumingrest.Quote;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationStartupConfig {

    protected final Log logger = LogFactory.getLog(getClass());

    /*
    * 初始化RestTemplate，便于后续的Http请求
    * */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        return builder.build();
    }

    /*
    * 1. SpringBoot会根据@Bean标签下的类[CommandLineRunner]的入参的类型[RestTemplate]，自动去autowire对象
    * 2. -> 为 lambda表达式，当接口只有一个方法需要override的时候，可以直接用lambda表达式匿名生成对象
    * 3. CommandLineRunner会在程序开始根据@Order顺序自动执行
    * */
    @Bean
    @Order(value = 3)
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            logger.info("creating a bean implementing CommandLineRunner with @Order(value = 3)");
            Quote quote = restTemplate.getForObject(
                    "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            logger.info(quote.toString());
        };
    }


}
