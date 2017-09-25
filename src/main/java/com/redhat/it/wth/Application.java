package com.redhat.it.wth;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.vertx.core.Vertx;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {

	@Autowired
	private ServerVerticle staticServer;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void deployVerticle() {
		Vertx.vertx().deployVerticle(staticServer);
	}
}