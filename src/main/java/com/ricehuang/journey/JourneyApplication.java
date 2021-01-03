package com.ricehuang.journey;

import com.ricehuang.journey.consumingrest.Quote;
import com.ricehuang.journey.restservice.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Order(value = 1)
public class JourneyApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(JourneyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JourneyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("add command line runner by implementing CommandLineRunner under tag @SpringBootApplication with @Order(value = 1)");
	}
}
