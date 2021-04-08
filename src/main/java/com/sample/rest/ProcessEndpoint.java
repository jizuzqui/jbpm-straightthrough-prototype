package com.sample.rest;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.sample.service.StraightThroughService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * ProcessEndpoint
 */
@Path("/business-process-management/v1/process-models/{processId}/versions/{version}:instance")
public class ProcessEndpoint {

    @Autowired
    StraightThroughService straightThroughService;
	private Map<String, Object> processResult;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startProcess(@PathParam("processId") String processId, @PathParam("version") String version, @RequestBody Map<String, Object> payload) {
        
    	
    	//processResult = straightThroughService.startProcess(processId, Collections.emptyMap());
    	processResult = straightThroughService.startProcess(processId, payload);
        return Response.ok(processResult).build();
    }
    
}