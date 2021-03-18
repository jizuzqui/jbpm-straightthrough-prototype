package com.sample.springapp.config;

import com.sample.rest.CheckEndpoint;
import com.sample.rest.ProcessEndpoint;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.sample.service")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        
        register(ProcessEndpoint.class);
        register(CheckEndpoint.class);
    }
}
