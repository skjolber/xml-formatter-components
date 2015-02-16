package com.greenbird.xmlns.schema.logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.greenbird.xml.prettyprint.jaxrs.PrettyPrint;

@Path("logger2")
@PrettyPrint(anonymizeFilters = {"/*/address"})
public class LoggerResource2 {

	@Produces("application/xml")
	@Consumes("application/xml")
    @Path("performLogMessageObject")
    @POST
    public PerformLogMessageResponse performLogMessageObject(PerformLogMessageRequest r) {
    	
    	PerformLogMessageResponse response = new PerformLogMessageResponse();
    	response.setStatus(1);
    	
        return response;
    }

}