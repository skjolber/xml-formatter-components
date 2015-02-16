package com.greenbird.xmlformatter.cxf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Logging;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Inteceptor tests. <br/><br/>
 * 
 * Notes: Limit seems to behave differently for writer vs outputstream.
 * 
 * @author thomas
 *
 */

public class LoggingProviderTest extends AbstractInterceptorTest {

	@Before
	public void load() throws Exception {
	}
	
	@Test
    public void testConstructors() throws Exception {
		LoggingFeature loggingFeature1 = new LoggingFeature();
		LoggingFeature loggingFeature2 = new LoggingFeature(1024);
		LoggingFeature loggingFeature3 = new LoggingFeature("in", "out");
		LoggingFeature loggingFeature4 = new LoggingFeature("in", "out", 1024);
		LoggingFeature loggingFeature5 = new LoggingFeature("in", "out", 1024);
		LoggingFeature loggingFeature6 = new LoggingFeature("in", "out", 1024, true);
		LoggingFeature loggingFeature7 = new LoggingFeature("in", "out", 1024, true, true);
		LoggingFeature loggingFeature8 = new LoggingFeature(new Logging() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			
			public boolean showBinary() {
				return false;
			}
			
			@Override
			public boolean pretty() {
				return false;
			}
			
			@Override
			public String outLocation() {
				return null;
			}
			
			@Override
			public int limit() {
				return 1024;
			}
			
			@Override
			public String inLocation() {
				return null;
			}
		});

		Assert.assertEquals(64*1024, loggingFeature1.getLimit());
		Assert.assertEquals(1024, loggingFeature2.getLimit());
		Assert.assertEquals(64*1024, loggingFeature3.getLimit());
		Assert.assertEquals(1024, loggingFeature4.getLimit());
		Assert.assertEquals(1024, loggingFeature5.getLimit());
		Assert.assertEquals(1024, loggingFeature6.getLimit());
		Assert.assertEquals(1024, loggingFeature7.getLimit());
		Assert.assertEquals(1024, loggingFeature8.getLimit());

    }

	@Test
    public void testConstructorBordercases() throws Exception {
		LoggingFeature loggingFeature1 = new LoggingFeature("<stdout>", null);
		LoggingFeature loggingFeature2 = new LoggingFeature(null, "<stdout>");
		LoggingFeature loggingFeature3 = new LoggingFeature(null, null, 1024);
		
        Bus provider = mock(Bus.class);
        
        when(provider.getInInterceptors()).thenReturn(new ArrayList<Interceptor<? extends Message>>());
        when(provider.getInFaultInterceptors()).thenReturn(new ArrayList<Interceptor<? extends Message>>());
        when(provider.getOutInterceptors()).thenReturn(new ArrayList<Interceptor<? extends Message>>());
        when(provider.getOutFaultInterceptors()).thenReturn(new ArrayList<Interceptor<? extends Message>>());
        
		loggingFeature1.initialize(provider);
		loggingFeature2.initialize(provider);
		loggingFeature3.initialize(provider);
		
	}

	@Test
    public void testSettersAndGetters() throws Exception {
		LoggingFeature loggingFeature = new LoggingFeature();
		
		loggingFeature.setLimit(1024);
		Assert.assertEquals(1024, loggingFeature.getLimit());

		loggingFeature.setRobustness(true);
		Assert.assertEquals(true, loggingFeature.isRobustness());
		
		loggingFeature.setIgnoreWhitespace(true);
		Assert.assertEquals(true, loggingFeature.isIgnoreWhitespace());

		loggingFeature.setPrettyPrintCData(true);
		Assert.assertEquals(true, loggingFeature.isPrettyPrintCData());

		loggingFeature.setPrettyPrintComments(true);
		Assert.assertEquals(true, loggingFeature.isPrettyPrintComments());
		
		loggingFeature.setXmlDeclaration(true);
		Assert.assertEquals(true, loggingFeature.isXmlDeclaration());
		
		loggingFeature.setMaxTextNodeLength(1024);
		Assert.assertEquals(1024, loggingFeature.getMaxTextNodeLength());
		
		loggingFeature.setMaxCDATANodeLength(1024);
		Assert.assertEquals(1024, loggingFeature.getMaxCDATANodeLength());
		
		List<String> filters = new ArrayList<String>();
		filters.add("/a");
		
		loggingFeature.setAnonymizeFilters(filters);
		Assert.assertEquals(filters, loggingFeature.getAnonymizeFilters());

		loggingFeature.setAnonymizeFilters(null);
		Assert.assertTrue(loggingFeature.getAnonymizeFilters().isEmpty());

		loggingFeature.setPruneFilters(filters);
		Assert.assertEquals(filters, loggingFeature.getPruneFilters());

		loggingFeature.setPruneFilters(null);
		Assert.assertTrue(loggingFeature.getPruneFilters().isEmpty());

		loggingFeature.setIndentationMultiplier(2);
		Assert.assertEquals(2, loggingFeature.getIndentationMultiplier());

		loggingFeature.setIndentationCharacter(' ');
		Assert.assertEquals(' ', loggingFeature.getIndentationCharacter());
		
	}

}
