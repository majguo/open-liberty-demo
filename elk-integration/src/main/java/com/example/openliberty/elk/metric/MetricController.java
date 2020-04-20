package com.example.openliberty.elk.metric;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Random;

@Path("/metric")
@ApplicationScoped //Required for @Gauge
public class MetricController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Inject
    @Metric(name = "endpoint_counter")
    private Counter counter;

    @Path("timed")
    @Timed(name = "timed_request")
    @GET
    public String timedRequest() {
        // Demo, not production style
        int wait = new Random().nextInt(1000);
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            // Demo
        	log.error("Exception occured in timedRequest() method: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }

        log.info("Request is used in statistics, check with the Metrics call.");
        return "Request is used in statistics, check with the Metrics call.";
    }


    @Path("increment")
    @GET
    public String doIncrement() {
        counter.inc();
        
        log.info("The counter is incremented and the current value is: {}", counter.getCount());
        return String.valueOf(counter.getCount());
    }

    @Gauge(name = "counter_gauge", unit = MetricUnits.NONE)
    private long getCustomerCount() {
        return counter.getCount();
    }
}
