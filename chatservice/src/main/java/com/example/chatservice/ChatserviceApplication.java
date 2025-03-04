package com.example.chatservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ChatserviceApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ChatserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReadyEvent() {
		log.info(env.getProperty("spring.application.name"));
		log.info(env.getProperty("spring.datasource.url"));
	}

}
