package com.github.cmcrobotics.shadowtheater.ide;

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
@ConfigurationProperties(prefix = "cineglobe.ide",ignoreUnknownFields = false)
public class FrontendConfigProperties {

	String home;
	boolean initializeHome;
}
