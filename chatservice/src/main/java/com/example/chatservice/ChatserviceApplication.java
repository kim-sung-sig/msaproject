package com.example.chatservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ChatserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatserviceApplication.class, args);
	}

		@Value("${spring.application.name}")
	private String appName;

	// 서버 포트
	@Value("${server.port}")
	private String serverPort;

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReadyEvent() {
		log.info("Application started ... ");
		log.info("Application Name: {}", appName);
		log.info("Server Port: {}", serverPort);
	}
}
