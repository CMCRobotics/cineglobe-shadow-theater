package com.github.cmcrobotics.shadowtheater.daemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableJpaRepositories("com.github.chibyhq.store.model.repositories")
@ComponentScan(basePackages = { "com.github.chibyhq.*", "com.github.cmcrobotics.shadowtheater.*" })
@EntityScan("com.github.chibyhq.playar.model")
@EnableSpringDataWebSupport
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
}
