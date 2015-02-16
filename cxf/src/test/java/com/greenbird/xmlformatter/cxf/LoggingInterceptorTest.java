package com.greenbird.xmlformatter.cxf;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;

/**
 * 
 * Inteceptor tests. <br/><br/>
 * 
 * Notes: Limit seems to behave differently for writer vs outputstream.
 * 
 * @author thomas
 *
 */

public class LoggingInterceptorTest extends AbstractInterceptorTest {
	
	@Test
    public void testMockLoggerInputStream() throws Exception {
		
		String[] inputs = new String[]{request1, response1, "", null};
		
		for(String input : inputs) {
	        LoggingInInterceptor interceptor = new LoggingInInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageIn(interceptor, input, "text/xml", false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();

	        if(input != null) {
		        StringBuilder builder = new StringBuilder();
		        if(prettyPrinter.process(input, builder)) {
		        	Assert.assertTrue(logStatement.contains(builder.toString()));
		        } else {
		        	Assert.fail();
		        }
	        }
		}
    }
	
	@Test
    public void testMockLoggerInputStreamInvalid() throws Exception {
		
        LoggingInInterceptor interceptor = new LoggingInInterceptor(prettyPrinter);
        interceptor.setPrettyLogging(true);

        Logger logger = mock(Logger.class);
        interceptor.setLogger(logger);

    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        fireMessageIn(interceptor, invalid, "text/xml", false);
        
        verify(logger, times(1)).log(captor.capture());
        
        LogRecord value = captor.getValue();
        String logStatement = value.getMessage();
        
       	Assert.assertTrue(logStatement.contains(invalid));
    }
	
	@Test
    public void testMockLoggerInputStreamNonXML() throws Exception {
		String[] mimeTypes = new String[]{null, "text/plain", "text/xml; multipart/related"};
		
		for(String mimeType : mimeTypes) {
	        LoggingInInterceptor interceptor = new LoggingInInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageIn(interceptor, invalid, mimeType, false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();
	        
	       	Assert.assertTrue(logStatement.contains(invalid));
		}
    }

	@Test
    public void testMockLoggerInputStreamNonXMLLimit() throws Exception {
		int[] limits = new int[]{-1, invalid.length() / 2, invalid.length()};
		String[] mimeTypes = new String[]{null, "text/plain", "text/xml; multipart/related"};
		
		for(int limit : limits) {
			for(String mimeType : mimeTypes) {
		        LoggingInInterceptor interceptor = new LoggingInInterceptor(prettyPrinter);
		        interceptor.setPrettyLogging(true);
		        interceptor.setLimit(limit);
		        
		        Logger logger = mock(Logger.class);
		        interceptor.setLogger(logger);
		
		    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
		        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
		        
		        fireMessageIn(interceptor, invalid, mimeType, false);
		        
		        verify(logger, times(1)).log(captor.capture());
		        
		        LogRecord value = captor.getValue();
		        String logStatement = value.getMessage();
		        
	        	if(limit == -1) {
	        		Assert.assertTrue(logStatement.contains(invalid));
	        	} else {
	        		Assert.assertTrue(logStatement.contains(invalid.substring(0, limit)));
	        	}
			}
		}
    }

	
	@Test
    public void testMockLoggerInputStreamLimit() throws Exception {
		
		int[] limits = new int[]{-1, request1.length() / 2, request1.length()};
		
		for(int limit : limits) {

	        LoggingInInterceptor interceptor = new LoggingInInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	        interceptor.setLimit(limit);
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageIn(interceptor, request1, "text/xml", false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(request1, builder)) {
	        	if(limit == -1) {
	        		Assert.assertTrue(logStatement.contains(builder.toString()));
	        	} else {
	        		Assert.assertTrue(logStatement.contains(builder.toString().substring(0, limit)));
	        	}
	        } else {
	        	Assert.fail();
	        }
		}
    }


	@Test
    public void testMockLoggerOutputStream() throws Exception {
    	
		String[] inputs = new String[]{request1, response1, ""};
		
		for(String input : inputs) {

	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageByteOut(interceptor, input, "text/xml", false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();
	        
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
    	
        // adding logging interceptors to print the received/sent messages
        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(prettyPrinter);
        interceptor.setPrettyLogging(true);

        Logger logger = mock(Logger.class);
        interceptor.setLogger(logger);

    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        fireMessageByteOut(interceptor, invalid, "text/xml", false);
        
        verify(logger, times(1)).log(captor.capture());
        
        LogRecord value = captor.getValue();
        String logStatement = value.getMessage();
        
        Assert.assertTrue(logStatement.contains(invalid));
    }
	
	@Test
    public void testMockLoggerOutputStreamNonXML() throws Exception {
    	
		String[] mimeTypes = new String[]{null, "text/plain", "text/xml; multipart/related"};

		for(String mimeType : mimeTypes) {

	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageByteOut(interceptor, invalid, mimeType, false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();
	        
	        Assert.assertTrue(logStatement.contains(invalid));
		}
    }

	@Test
    public void testMockLoggerOutputStreamNonXMLLimit() throws Exception {
    	
		int[] limits = new int[]{-1, 0, invalid.length() / 2, invalid.length()};
		String[] mimeTypes = new String[]{null, "text/plain", "text/xml; multipart/related"};

		for(int limit : limits) {
			for(String mimeType : mimeTypes) {
	
		        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(prettyPrinter);
		        interceptor.setPrettyLogging(true);
		        interceptor.setLimit(limit);
		        
		        Logger logger = mock(Logger.class);
		        interceptor.setLogger(logger);
		
		    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
		        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
		        
		        fireMessageByteOut(interceptor, invalid, mimeType, false);
		        
		        verify(logger, times(1)).log(captor.capture());
		        
		        LogRecord value = captor.getValue();
		        String logStatement = value.getMessage();
		        
		        if(limit == -1) {
		        	Assert.assertTrue(logStatement.contains(invalid));
		        } else {
		        	Assert.assertTrue(logStatement.contains(invalid.substring(0, limit)));
		        }
			}
		}
    }

	@Test
    public void testMockLoggerOutputStreamLimit() throws Exception {

		int[] limits = new int[]{-1, response1.length() / 2, response1.length()};
		
		for(int limit : limits) {

	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(prettyPrinter);
	        interceptor.setPrettyLogging(true);
	        interceptor.setLimit(limit);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	
	    	ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        fireMessageByteOut(interceptor, response1, "text/xml", false);
	        
	        verify(logger, times(1)).log(captor.capture());
	        
	        LogRecord value = captor.getValue();
	        String logStatement = value.getMessage();
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(response1, builder)) {
		        if(limit == -1) {
		        	Assert.assertTrue(logStatement.contains(builder.toString()));
		        } else {
		        	Assert.assertTrue(logStatement.contains(builder.toString().substring(0, limit)));
		        }
	        } else {
	        	Assert.fail();
	        }
		}
    }

	
	@Test
    public void testMockLoggerWriter() throws Exception {
    	// directly set the writer
		
		String[] inputs = new String[]{request1, response1, ""};
		
		for(String input : inputs) {

			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(writer, prettyPrinter);
	        interceptor.setPrettyLogging(true);
	
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        StringBuilder fireMessageWriterOut = fireMessageWriterOut(interceptor, input, "text/xml");
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(input, builder)) {
	        	String string;
	        	if(this.writer) {
	        		string = writer.toString();
	        	} else {
	    	        string = fireMessageWriterOut.toString();
	        	}
        		Assert.assertTrue(string, string.contains(builder.toString()));
	        } else {
	        	Assert.fail();
	        }
	        
		}
    }

	@Test
    public void testMockLoggerWriterInvalid() throws Exception {
    	// directly set the writer
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(writer, prettyPrinter);
        interceptor.setPrettyLogging(true);

        Logger logger = mock(Logger.class);
        interceptor.setLogger(logger);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        StringBuilder fireMessageWriterOut = fireMessageWriterOut(interceptor, invalid, "text/xml");
        
    	String string;
    	if(this.writer) {
    		string = writer.toString();
    	} else {
	        string = fireMessageWriterOut.toString();
    	}

        Assert.assertTrue(string.contains(invalid));
    }
	
	@Test
    public void testMockLoggerWriterNonXML() throws Exception {
    	// directly set the writer
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(writer, prettyPrinter);
        interceptor.setPrettyLogging(true);

        Logger logger = mock(Logger.class);
        interceptor.setLogger(logger);
        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
        
        StringBuilder fireMessageWriterOut = fireMessageWriterOut(interceptor, invalid, null);
        
    	String string;
    	if(this.writer) {
    		string = writer.toString();
    	} else {
	        string = fireMessageWriterOut.toString();
    	}

        Assert.assertTrue(string.contains(invalid));
    }

	@Test
    public void testMockLoggerWriterNonXMLLimit() throws Exception {
    	// directly set the writer
		
		int[] limits = new int[]{0, invalid.length() / 2, invalid.length()};
		
		for(int limit : limits) {

			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(writer, prettyPrinter);
	        interceptor.setPrettyLogging(true);
	        interceptor.setLimit(limit);
	        
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        StringBuilder fireMessageWriterOut = fireMessageWriterOut(interceptor, invalid, null);

        	String string;
        	if(this.writer) {
        		string = writer.toString();
        	} else {
    	        string = fireMessageWriterOut.toString();
        	}

	        Assert.assertTrue(string.contains(invalid.substring(0, limit)));
		}
    }

	
	@Test
    public void testMockLoggerWriterLimit() throws Exception {
    	// directly set the writer
		
		int[] limits = new int[]{-1, response1.length() / 2, response1.length()};
		
		for(int limit : limits) {

			StringWriter stringWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(stringWriter);
	        LoggingOutInterceptor interceptor = new LoggingOutInterceptor(writer, prettyPrinter);
	        interceptor.setPrettyLogging(true);
	        interceptor.setLimit(limit);
	        
	        Logger logger = mock(Logger.class);
	        interceptor.setLogger(logger);
	        when(logger.isLoggable(any(Level.class))).thenReturn(Boolean.TRUE);
	        
	        StringBuilder fireMessageWriterOut = fireMessageWriterOut(interceptor, response1, "text/xml");
	        
	        StringBuilder builder = new StringBuilder();
	        if(prettyPrinter.process(response1, builder)) {
	        	String string;
	        	if(this.writer) {
	        		string = writer.toString();
	        	} else {
	    	        string = fireMessageWriterOut.toString();
	        	}

	        	if(limit == -1) {
	        		Assert.assertTrue(string.contains(builder.toString()));
	        	} else {
	        		Assert.assertTrue(string.contains(builder.toString().substring(0, limit)));
	        	}
	        } else {
	        	Assert.fail();
	        }
		}
    }
	
	@Test
    public void testInConstructors() throws Exception {
		LoggingInInterceptor interceptor = new LoggingInInterceptor();
		Assert.assertNotNull(interceptor.getLogger());
		Assert.assertNotNull(interceptor.getPrettyPrinter());
	}

	@Test
    public void testInSettersAndGetters() throws Exception {
		LoggingInInterceptor interceptor = new LoggingInInterceptor();
		
		PrettyPrinter prettyPrinter = new PlainPrettyPrinter(false);
		interceptor.setPrettyPrinter(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor.getPrettyPrinter());
	}

	@Test
    public void testOutConstructors() throws Exception {
		LoggingOutInterceptor interceptor = new LoggingOutInterceptor();
		Assert.assertNotNull(interceptor.getLogger());
		Assert.assertNotNull(interceptor.getPrettyPrinter());
	}

	@Test
    public void testOutSettersAndGetters() throws Exception {
		LoggingOutInterceptor interceptor = new LoggingOutInterceptor();

		PrettyPrinter prettyPrinter = new PlainPrettyPrinter(false);
		interceptor.setPrettyPrinter(prettyPrinter);
		Assert.assertSame(prettyPrinter, interceptor.getPrettyPrinter());
	}

}