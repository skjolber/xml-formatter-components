package com.greenbird.xmlformatter.cxf;

import junit.framework.Assert;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.PrettyPrinterFactory;

public class HelloWorldEndToEndTest {

	private static final String ADDRESS = "http://localhost:9000/helloWorld";

	private PrettyPrinter prettyPrinter;
	
	@Before
	public void load() throws Exception {
    	PrettyPrinterFactory prettyPrinterFactory = new PrettyPrinterFactory();
    	
    	prettyPrinterFactory.setPrettyPrintCData(true);
    	prettyPrinterFactory.setMaxCDATANodeLength(1024);
    	prettyPrinterFactory.setMaxTextNodeLength(1024);
    	
    	prettyPrinter = prettyPrinterFactory.newPrettyPrinter();  
	}
	@Test
    public void testAddInterceptors() throws Exception {
        // start the HelloWorld service using jaxWsServerFactoryBean
		for(boolean logging : new boolean[]{true, false}) {
	        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
	
	        // adding logging interceptors to print the received/sent messages
	        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor(prettyPrinter);
	        loggingInInterceptor.setPrettyLogging(logging);
	        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor(prettyPrinter);
	        loggingOutInterceptor.setPrettyLogging(logging);
	
	        jaxWsServerFactoryBean.getInInterceptors().add(loggingInInterceptor);
	        jaxWsServerFactoryBean.getOutInterceptors().add(loggingOutInterceptor);
	
	        HelloWorldImpl implementor = new HelloWorldImpl();
	        jaxWsServerFactoryBean.setServiceClass(HelloWorld.class);
	        jaxWsServerFactoryBean.setAddress(ADDRESS);
	        jaxWsServerFactoryBean.setServiceBean(implementor);
	        Server server = jaxWsServerFactoryBean.create();
	        
	        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	        factory.setServiceClass(HelloWorld.class);
	        factory.setAddress(ADDRESS);
	        HelloWorld client = (HelloWorld) factory.create();
	
	        String reply = client.sayHello("Thomas");
	        
	        server.stop();
	        
	        Assert.assertEquals("Hello Thomas", reply);
		}
    }

	@Test
    public void testFeature() throws Exception {
        // start the HelloWorld service using jaxWsServerFactoryBean
		
		for(boolean logging : new boolean[]{true, false}) {

	        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
	        
	        LoggingFeature feature = new LoggingFeature();
	        jaxWsServerFactoryBean.getFeatures().add(feature);
	        feature.setPrettyLogging(logging);
	        Assert.assertEquals(logging, feature.isPrettyLogging());
	        
	        HelloWorldImpl implementor = new HelloWorldImpl();
	        jaxWsServerFactoryBean.setServiceClass(HelloWorld.class);
	        jaxWsServerFactoryBean.setAddress(ADDRESS);
	        jaxWsServerFactoryBean.setServiceBean(implementor);
	        Server server = jaxWsServerFactoryBean.create();
	        
	        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	        factory.setServiceClass(HelloWorld.class);
	        factory.setAddress(ADDRESS);
	        HelloWorld client = (HelloWorld) factory.create();
	
	        String reply = client.sayHello("Thomas");
	        
	        server.stop();
	
	        Assert.assertEquals("Hello Thomas", reply);
		}
    }
	
}