package com.greenbird.xmlformatter.cxf;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.message.Message;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class XMLLoggingInterceptorTest extends AbstractInterceptorTest {

	@Test
    public void testMockLoggerInputStream() throws Exception {
		
		String[] inputs = new String[]{request1, response1, ""};
		
		for(String input : inputs) {
	        Logger logger = mock(Logger.class);
	        
			XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
				
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageIn(interceptor, input, "text/xml", false);
	        
	        verify(logger, times(1)).log(logLevelCaptor.capture(), logStatementCaptor.capture());

        	Assert.assertEquals(Level.INFO, logLevelCaptor.getValue());

	        String logStatement = logStatementCaptor.getValue();
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(input, builder)) {
	        	Assert.assertTrue(logStatement.contains(builder.toString()));
	        } else {
	        	Assert.fail();
	        }
		}
    }
	
	@Test
    public void testMockLoggerInputStreamInvalid() throws Exception {
		
		for(Boolean headers : new Boolean[]{true, false, null}) {
	        Logger logger = mock(Logger.class);
	        
			XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageIn(interceptor, invalid, "text/xml", headers);
	        
	        verify(logger, times(1)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
	
	    	Assert.assertEquals(Level.WARNING, logLevelCaptor.getValue());
	
	        String logStatement = logStatementCaptor.getValue();
	        
	       	Assert.assertTrue(logStatement.contains(invalid));
		}
    }
	
	
	@Test
    public void testMockLoggerInputStreamException() throws Exception {
		
		for(boolean headers : new boolean[]{true, false}) {
	        Logger logger = mock(Logger.class);
	        
			XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);

	        InputStream inputStream = mock(InputStream.class);
	        when(inputStream.read(any(byte[].class))).thenThrow(new IOException());

	        try {
	        	fireMessageInput(interceptor, inputStream, "text/xml", headers);
	        	
	        	Assert.fail();
	        } catch(Exception e) {
	        	
	        }
		}
    }

	@Test
    public void testMockLoggerInputStreamHighLogLevel() throws Exception {
		
		for(boolean headers : new boolean[]{true, false}) {
	        Logger logger = mock(Logger.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.FALSE);
	        
			XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        
	        fireMessageIn(interceptor, invalid, "text/xml", headers);
	        
	        verify(logger, times(0)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
		}
    }
	
	@Test
    public void testMockLoggerNoInputContent() throws Exception {
		
		for(boolean headers : new boolean[]{true, false}) {
	        Logger logger = mock(Logger.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
			XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        
	        fireMessageInput(interceptor, null, null, headers);
	        
	        verify(logger, times(0)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
		}
    }
	
	@Test
	public void testInInterceptorConstructors() {
		
        Logger logger = mock(Logger.class);

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
	public void testInInterceptorSettersAndGetters() {
		XMLLoggingInInterceptor interceptor = new XMLLoggingInInterceptor();

		String name = "InterceptorName";
		interceptor.setName(name);
		Assert.assertEquals(name, interceptor.getName());
		
		interceptor.setPrettyPrinter(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor.getPrettyPrinter());
		
        Logger logger = Logger.getLogger(getClass().getName());
		interceptor.setLogger(logger);
		Assert.assertSame(logger, interceptor.getLogger());

		interceptor.setReformatFailureLevel(Level.ALL);
		Assert.assertSame(Level.ALL, interceptor.getReformatFailureLevel());
		
		interceptor.setReformatSuccessLevel(Level.ALL);
		Assert.assertSame(Level.ALL, interceptor.getReformatSuccessLevel());

	}


	@Test
    public void testMockLoggerOutputStream() throws Exception {
    	
		String[] inputs = new String[]{request1, response1, ""};
		
		for(String input : inputs) {
	        Logger logger = mock(Logger.class);
	        
	        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
				
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageByteOut(interceptor, input, "text/xml", false);
	        
	        verify(logger, times(1)).log(logLevelCaptor.capture(), logStatementCaptor.capture());

        	Assert.assertEquals(Level.INFO, logLevelCaptor.getValue());

	        String logStatement = logStatementCaptor.getValue();
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(input, builder)) {
	        	Assert.assertTrue(logStatement.contains(builder.toString()));
	        } else {
	        	Assert.fail();
	        }
		}
		
		
    }
	

	@Test
    public void testMockLoggerOutputStreamInvalid() throws Exception {
    	
		for(Boolean headers : new Boolean[]{true, false, null}) {
			Logger logger = mock(Logger.class);
	        
	        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
				
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageByteOut(interceptor, invalid, "text/xml", headers);
	        
	        verify(logger, times(1)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
	
	    	Assert.assertEquals(Level.WARNING, logLevelCaptor.getValue());
	
	        String logStatement = logStatementCaptor.getValue();
	        
	        Assert.assertTrue(logStatement.contains(invalid));
		}
    }
	
	@Test
    public void testMockLoggerOutputStreamException() throws Exception {
		
        Logger logger = mock(Logger.class);
        
        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);

        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);

        OutputStream outputStream = mock(OutputStream.class);
        
        doThrow(new IOException()).when(outputStream).write(any(int.class));
        doThrow(new IOException()).when(outputStream).write(any(byte[].class));
        
        try {
        	fireMessageByteOut(interceptor, outputStream, request1, "text/xml", false);
        	
        	Assert.fail();
        } catch(Exception e) {
        	
        }
    }

	@Test
	public void testOutInterceptorConstructors() {
		
        Logger logger = mock(Logger.class);

		XMLLoggingOutInterceptor interceptor1 = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
		Assert.assertSame(prettyPrinter, interceptor1.getPrettyPrinter());
		Assert.assertSame(logger, interceptor1.getLogger());
		Assert.assertEquals(Level.INFO, interceptor1.getReformatSuccessLevel());
		Assert.assertEquals(Level.WARNING, interceptor1.getReformatFailureLevel());

		XMLLoggingOutInterceptor interceptor2 = new XMLLoggingOutInterceptor(logger, Level.INFO,  Level.ALL);
		Assert.assertSame(logger, interceptor2.getLogger());
		Assert.assertEquals(Level.INFO, interceptor2.getReformatSuccessLevel());
		Assert.assertEquals(Level.ALL, interceptor2.getReformatFailureLevel());
		
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
	public void testOutInterceptorSettersAndGetters() {
		XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor();

		String name = "InterceptorName";
		interceptor.setName(name);
		Assert.assertEquals(name, interceptor.getName());
		
		interceptor.setPrettyPrinter(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor.getPrettyPrinter());
		
        Logger logger = Logger.getLogger(getClass().getName());
		interceptor.setLogger(logger);
		Assert.assertSame(logger, interceptor.getLogger());

		interceptor.setReformatFailureLevel(Level.ALL);
		Assert.assertSame(Level.ALL, interceptor.getReformatFailureLevel());
		
		interceptor.setReformatSuccessLevel(Level.ALL);
		Assert.assertSame(Level.ALL, interceptor.getReformatSuccessLevel());

	}

	@Test
    public void testMockLoggerOutputStreamHighLogLevel() throws Exception {
		
		for(boolean headers : new boolean[]{true, false}) {
	        Logger logger = mock(Logger.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.FALSE);
	        
	        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        
	        fireMessageByteOut(interceptor, invalid, "text/xml", headers);
	        
	        verify(logger, times(0)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
		}
    }
	
	@Test
    public void testMockLoggerNoOutputContent() throws Exception {
		
		for(boolean headers : new boolean[]{true, false}) {
	        Logger logger = mock(Logger.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING);
	
	    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
	    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
	        
	        fireMessageByteOut(interceptor, null, null, null, headers);
	        
	        verify(logger, times(0)).log(logLevelCaptor.capture(), logStatementCaptor.capture());
		}
    }
	
	@Test
    public void testMockLoggerLoggingProperties() throws Exception {
		
        Logger logger = mock(Logger.class);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        XMLLoggingOutInterceptor interceptor = new XMLLoggingOutInterceptor(prettyPrinter, logger, Level.INFO,  Level.WARNING) {
        	@Override
        	protected void logProperties(StringBuilder buffer, Message message) {
        		buffer.append(" A=B");
        	}
        };
        
    	ArgumentCaptor<String> logStatementCaptor = ArgumentCaptor.forClass(String.class);
    	ArgumentCaptor<Level> logLevelCaptor = ArgumentCaptor.forClass(Level.class);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        fireMessageByteOut(interceptor, request1, "text/xml", false);
        
        verify(logger, times(1)).log(logLevelCaptor.capture(), logStatementCaptor.capture());

    	Assert.assertEquals(Level.INFO, logLevelCaptor.getValue());

        String logStatement = logStatementCaptor.getValue();
        
        StringBuilder builder = new StringBuilder();
        if(prettyPrinter.process(request1, builder)) {
        	Assert.assertTrue(logStatement.contains(builder.toString()));
        	Assert.assertTrue(logStatement.contains(" A=B"));
        } else {
        	Assert.fail();
        }
    }

}