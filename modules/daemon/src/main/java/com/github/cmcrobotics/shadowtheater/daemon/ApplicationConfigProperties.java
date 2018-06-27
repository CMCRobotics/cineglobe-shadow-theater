package com.github.cmcrobotics.shadowtheater.daemon;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "cineglobe.daemon",ignoreUnknownFields = false)
public class ApplicationConfigProperties {

	String home;
	boolean initializeHome;
	
	
	String baseUri = "http://localhost:8080/data";
}
