package com.example.openliberty.aad.oidc.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class ServiceReadyHealthCheck implements HealthCheck {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Override
    public HealthCheckResponse call() {
    	log.info("{} is ready!", ServiceReadyHealthCheck.class.getSimpleName());

        return HealthCheckResponse.named(ServiceReadyHealthCheck.class.getSimpleName())
            .withData("ready",true).up().build();

    }
}
