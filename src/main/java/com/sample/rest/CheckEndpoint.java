package com.sample.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/check")
public class CheckEndpoint {

    @GET
    public Response startProcess() {
        return Response.ok().build();
    }
    
}
