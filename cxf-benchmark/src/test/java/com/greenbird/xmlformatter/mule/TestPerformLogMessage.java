
package com.greenbird.xmlformatter.mule;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Properties;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

import com.greenbird.xmlns.schema.logger.LoggerPort;
import com.greenbird.xmlns.schema.logger.PerformLogMessageRequest;
import com.greenbird.xmlns.schema.logger.PerformLogMessageResponse;

public class TestPerformLogMessage extends FunctionalTestCase {
	
	private LoggerPort xmlformatterCxfService;
	private LoggerPort xmlformatterMuleCxfService;
	private LoggerPort cxfService;
	private LoggerPort cxfStAXService;
	private LoggerPort noLoggerService;
	
    @Override protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Before
    public void createService() throws IOException {
    	Properties p = new Properties();
    	p.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));

    	xmlformatterCxfService = getService(LoggerPort.class, p.get("benchmark.cxf.xmlformatter.inbound.url").toString());
    	xmlformatterMuleCxfService = getService(LoggerPort.class, p.get("benchmark.mule.cxf.xmlformatter.inbound.url").toString());
    	cxfService = getService(LoggerPort.class, p.get("benchmark.cxf.inbound.url").toString());
    	noLoggerService = getService(LoggerPort.class, p.get("benchmark.inbound.url").toString());
    	cxfStAXService = getService(LoggerPort.class, p.get("benchmark.cxf.stax.inbound.url").toString());
    }

    @Test
    public void testLoggingCXF() throws Exception {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        
        PerformLogMessageResponse performLogMessage = cxfService.performLogMessage(request);
        assertNotNull(performLogMessage);
    }

    @Test
    public void testLoggingXmlformatterCXF() throws Exception {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        
        PerformLogMessageResponse performLogMessage = xmlformatterCxfService.performLogMessage(request);
        assertNotNull(performLogMessage);
    }

    @Test
    public void testLoggingXmlformatterMuleCXF() throws Exception {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        
        PerformLogMessageResponse performLogMessage = xmlformatterMuleCxfService.performLogMessage(request);
        assertNotNull(performLogMessage);
    }

    @Test
    public void testLoggingNoLogging() throws Exception {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        
        PerformLogMessageResponse performLogMessage = noLoggerService.performLogMessage(request);
        assertNotNull(performLogMessage);
    }

    @Test
    public void testLoggingCXFStax() throws Exception {
        PerformLogMessageRequest request = new PerformLogMessageRequest();
        
        PerformLogMessageResponse performLogMessage = cxfStAXService.performLogMessage(request);
        assertNotNull(performLogMessage);
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
