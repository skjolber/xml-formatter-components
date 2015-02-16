package com.greenbird.xml.prettyprint.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.greenbird.xml.prettyprint.jaxrs.PrettyPrint;

/**
 * Resource (exposed at "myResource" path)
 */
@Component
@Path("myResource")
public class MyResource {

	private static final String resourceValue = "<a><b>Making data fly</b></a>";
	
	public MyResource() {
	}
	
    @GET
    @Produces(MediaType.TEXT_XML)
    @PrettyPrint
    @Path("myMethod")
    public String myMethod() {
        return resourceValue;
    }
    
    @GET
    @Produces(MediaType.TEXT_XML)
    @PrettyPrint(anonymizeFilters = {"/a/b"})
    @Path("myFilterMethod")
    public String myFilerMethod() {
        return resourceValue;
    }
    
}
