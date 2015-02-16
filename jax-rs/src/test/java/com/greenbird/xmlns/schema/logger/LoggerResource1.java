package com.greenbird.xmlns.schema.logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.greenbird.xml.prettyprint.jaxrs.PrettyPrint;

@Path("logger1")
public class LoggerResource1 {

	@Produces("application/xml")
	@Consumes("application/xml")
    @Path("performLogMessageObject")
    @POST
    @PrettyPrint(anonymizeFilters = {"/performLogMessageRequest/address"}, maxCDATANodeLength = 1024, maxTextNodeLength = 1024)
    public PerformLogMessageResponse performLogMessageObject(PerformLogMessageRequest r) {
    	
    	PerformLogMessageResponse response = new PerformLogMessageResponse();
    	response.setStatus(1);
    	
        return response;
    }

	@Produces("text/plain")
	@Consumes("application/xml")
    @Path("performLogMessageRequestObject")
    @POST
    @PrettyPrint
    public Integer performLogMessageRequestObject(PerformLogMessageRequest r) {
        return new Integer(1);
    }

	@Consumes({MediaType.TEXT_PLAIN, MediaType.TEXT_XML})
	@Produces("application/xml")
    @Path("performLogMessageResponseObject")
	@POST
    @PrettyPrint
    public PerformLogMessageResponse performLogMessageResponseObject(String text) {
    	PerformLogMessageResponse response = new PerformLogMessageResponse();
    	response.setStatus(1);
    	
        return response;
    }

	@Consumes("application/xml")
    @Path("performLogMessageEmptyResponse")
    @POST
    @PrettyPrint
    public void performLogMessageEmptyResponse(PerformLogMessageRequest r) {
    }
	
    @Path("performLogMessageParameter/{message}")
    @GET
    @PrettyPrint
    public PerformLogMessageResponse performLogMessageParameter(@PathParam("message") String message) {
    	PerformLogMessageResponse response = new PerformLogMessageResponse();
    	response.setStatus(1);
    	
        return response;
    }
}