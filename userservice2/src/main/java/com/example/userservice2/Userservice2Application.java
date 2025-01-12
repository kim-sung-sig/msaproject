package com.example.userservice2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class Userservice2Application {

	public static void main(String[] args) {
		SpringApplication.run(Userservice2Application.class, args);
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
