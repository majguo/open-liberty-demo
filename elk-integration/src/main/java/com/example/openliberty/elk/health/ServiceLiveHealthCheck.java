package com.example.openliberty.elk.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class ServiceLiveHealthCheck implements HealthCheck {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public HealthCheckResponse call() {
    	log.info("{} is live!", ServiceLiveHealthCheck.class.getSimpleName());
    	
        return HealthCheckResponse.named(ServiceLiveHealthCheck.class.getSimpleName())
            .withData("live",true).up().build();

    }
}
