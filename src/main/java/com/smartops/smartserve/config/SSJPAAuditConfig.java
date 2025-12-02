package com.smartops.smartserve.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SSJPAAuditConfig {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new SSAuditorAware();
	}

}

class SSAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String user = "SmartOps";
		return Optional.ofNullable(user);
	}
}