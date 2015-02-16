package com.greenbird.xmlformatter.cxf;

import javax.jws.WebService;
 
@WebService(endpointInterface = "com.greenbird.xmlformatter.cxf.HelloWorld",
            serviceName = "HelloWorld")
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHello(String text) {
		return "Hello " + text;
	}
    
 
}