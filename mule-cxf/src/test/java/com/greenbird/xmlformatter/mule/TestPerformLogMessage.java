/*
 * Generated by the Mule project wizard. http://mule.mulesoft.org
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.greenbird.xmlformatter.mule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;
import com.greenbird.xmlformatter.mule.cxf.XMLLoggingInInterceptor;
import com.greenbird.xmlformatter.mule.cxf.XMLLoggingOutInterceptor;
import com.greenbird.xmlns.schema.logger.LoggerPort;
import com.greenbird.xmlns.schema.logger.PerformLogMessageRequest;
import com.greenbird.xmlns.schema.logger.PerformLogMessageResponse;

public class TestPerformLogMessage extends FunctionalTestCase {
	
	private LoggerPort loggerService;
	
    @Override protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Before
    public void createService() throws IOException {
    	Properties p = new Properties();
    	p.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));

    	loggerService = getService(LoggerPort.class, p.get("logger.inbound.url").toString());
    }
    
	@Test
	public void testInInterceptorConstructors() {
		
        Logger logger = mock(Logger.class);
        
        PrettyPrinter prettyPrinter = new PlainPrettyPrinter(false);

		XMLLoggingInInterceptor interceptor1 = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
		Assert.assertSame(prettyPrinter, interceptor1.getPrettyPrinter());
		Assert.assertSame(logger, interceptor1.getLogger());
		Assert.assertEquals(Level.INFO, interceptor1.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor1.getReformatFailureLevel());

		XMLLoggingInInterceptor interceptor2 = new XMLLoggingInInterceptor(logger, Level.INFO,  Level.ALL);
		Assert.assertSame(logger, interceptor2.getLogger());
		Assert.assertEquals(Level.INFO, interceptor2.getReformatSuccessLevel());
		Assert.assertEquals(Level.ALL, interceptor2.getReformatFailureLevel());
		
		XMLLoggingInInterceptor interceptor3 = new XMLLoggingInInterceptor(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor3.getPrettyPrinter());
		Assert.assertNotNull(interceptor3.getLogger());
		Assert.assertEquals(Level.INFO, interceptor3.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor3.getReformatFailureLevel());

		XMLLoggingInInterceptor interceptor4 = new XMLLoggingInInterceptor();
		Assert.assertNull(interceptor4.getPrettyPrinter());
		Assert.assertNotNull(interceptor4.getLogger());
		Assert.assertEquals(Level.INFO, interceptor4.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor4.getReformatFailureLevel());
		
	}

	@Test
	public void testOutInterceptorConstructors() {
		
        PrettyPrinter prettyPrinter = new PlainPrettyPrinter(false);
		
		XMLLoggingOutInterceptor interceptor3 = new XMLLoggingOutInterceptor(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor3.getPrettyPrinter());
		Assert.assertNotNull(interceptor3.getLogger());
		Assert.assertEquals(Level.INFO, interceptor3.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor3.getReformatFailureLevel());

		XMLLoggingOutInterceptor interceptor4 = new XMLLoggingOutInterceptor();
		Assert.assertNull(interceptor4.getPrettyPrinter());
		Assert.assertNotNull(interceptor4.getLogger());
		Assert.assertEquals(Level.INFO, interceptor4.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor4.getReformatFailureLevel());
		
	}
	
    @Test
    public void testLogging() throws Exception
    {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        request.setAddress("thomas.skjoelberg@greenbird.com");
        request.setSubject("Subject");
        request.setBody("Body");
        
        PerformLogMessageResponse performLogMessage = loggerService.performLogMessage(request);
        assertNotNull(performLogMessage);
        assertEquals(1, performLogMessage.getStatus());
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getService(Class<T> serviceClass, String url) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        factory.setAddress(url);
        factory.setServiceClass(serviceClass);
        
        //noinspection unchecked
        return (T) factory.create();
    }

}
