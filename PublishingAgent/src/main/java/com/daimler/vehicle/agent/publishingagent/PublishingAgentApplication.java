package com.daimler.vehicle.agent.publishingagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PublishingAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublishingAgentApplication.class, args);
	}

}
