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
import javax.ws.rs.core.Response;

import com.sample.service.StraightThroughService;


/**
 * ProcessEndpoint
 */

@Path("/business-process-management/v1/process-models/{processId}/versions/{version}:instance")
@RequestScoped
public class ProcessEndpoint {

    @Inject
    StraightThroughService straightThroughService;
	private Map<String, Object> processResult;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startProcess(@PathParam("processId") String processId, @PathParam("version") String version) {
        processResult = straightThroughService.startProcess(processId, Collections.emptyMap());
        return Response.ok(processResult).build();
    }
    
}