package com.example.openliberty.aad.oidc.resilient;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resilience")
@ApplicationScoped
public class ResilienceController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

    @Fallback(fallbackMethod = "fallback")
    @Timeout(500)
    @GET
    public String checkTimeout() {
        try {
            Thread.sleep(700L);
        } catch (InterruptedException e) {
            log.error("Exception occured in checkTimeout() method: {}", e.getLocalizedMessage());
        }
        return "Never from normal processing";
    }

    public String fallback() {
    	log.warn("Fallback answer due to timeout!");
        return "Fallback answer due to timeout";
    }
}
