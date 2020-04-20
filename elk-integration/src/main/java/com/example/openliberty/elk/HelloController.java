package com.example.openliberty.elk;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Path("/hello")
@Singleton
public class HelloController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @GET
    public String sayHello() {
    	log.info("sayHello() endpoint is called!");
    	
        return "Hello World";
    }
}
