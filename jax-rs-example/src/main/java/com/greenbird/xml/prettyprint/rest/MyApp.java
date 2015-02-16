package com.greenbird.xml.prettyprint.rest;

import org.glassfish.jersey.server.ResourceConfig;

import com.greenbird.xml.prettyprint.jaxrs.PrettyPrint;
import com.greenbird.xml.prettyprint.rest.resource.MyResource;

public class MyApp extends ResourceConfig {

	public MyApp() {
		packages(MyResource.class.getPackage().getName(), PrettyPrint.class.getPackage().getName());
	}
	
}
