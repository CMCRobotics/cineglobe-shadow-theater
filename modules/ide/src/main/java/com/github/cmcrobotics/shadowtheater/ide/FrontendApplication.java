package com.github.cmcrobotics.shadowtheater.ide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableMapRepositories("com.github.chibyhq.store.model.repositories")
@EnableSpringDataWebSupport
public class FrontendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}
}
