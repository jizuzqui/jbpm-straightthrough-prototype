package com.sample.rest;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sample.service.StraightThroughService;

/**
 * ProcessEndpoint
 */

@Path("/process")
@RequestScoped
public class ProcessEndpoint {

    @Inject
    StraightThroughService straightThroughService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> startProcess() {
        return straightThroughService.startProcess("sample-process", Collections.emptyMap());
    }
    
}