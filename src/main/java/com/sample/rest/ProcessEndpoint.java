package com.sample.rest;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sample.service.StraightThroughService;


/**
 * ProcessEndpoint
 */

@Path("/processes/{processId}")
@RequestScoped
public class ProcessEndpoint {

    @Inject
    StraightThroughService straightThroughService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> startProcess(@PathParam("processId") String processId) {
       // log.fine("Starting process with ID: "+processId);
        return straightThroughService.startProcess(processId, Collections.emptyMap());
    }
    
}