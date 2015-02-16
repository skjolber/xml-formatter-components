package com.greenbird.xmlformatter.cxf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Before;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.PrettyPrinterFactory;

public class AbstractInterceptorTest {

	protected static final String HEADER_LOCAL_NAME = "MessageId";
	protected static final String HEADER_OBJECT = "1234567890";
	
	
	protected String request1;
	protected String response1;
	protected String invalid;
	
	protected PrettyPrinter prettyPrinter;
	
	protected boolean writer = true;
	
	@Before
	public void load() throws Exception {
		
		// cxf does not handle a writer before version 2.6
    	Properties p = new Properties();
    	p.load(this.getClass().getClassLoader().getResourceAsStream("META-INF/maven/org.apache.cxf/cxf-api/pom.properties"));

    	DefaultArtifactVersion minVersion = new DefaultArtifactVersion("2.6");
    	DefaultArtifactVersion version = new DefaultArtifactVersion(p.getProperty("version"));
    	if (version.compareTo(minVersion) == -1) {
    		writer = false;
    	}
    	
		request1 = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("examples/request1.xml"), "UTF-8");
		response1 = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("examples/response1.xml"), "UTF-8");
		invalid = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("examples/invalid.xml"), "UTF-8");
		
    	PrettyPrinterFactory prettyPrinterFactory = new PrettyPrinterFactory();
    	
    	prettyPrinterFactory.setPrettyPrintCData(true);
    	prettyPrinterFactory.setMaxCDATANodeLength(1024);
    	prettyPrinterFactory.setMaxTextNodeLength(1024);
    	
    	prettyPrinter = prettyPrinterFactory.newPrettyPrinter();  
	}
	
	protected void fireMessageIn(Interceptor<Message> interceptor, String xml, String contentType, Boolean soapHeaders) throws UnsupportedEncodingException {
		if(xml != null) {
			fireMessageInput(interceptor, new ByteArrayInputStream(xml.getBytes("UTF-8") ), contentType, soapHeaders);
		} else {
			fireMessageInput(interceptor, null, contentType, soapHeaders);
		}
	}

	protected void fireMessageInput(Interceptor<Message> interceptor, InputStream inputStream, String contentType, Boolean soapHeaders) throws UnsupportedEncodingException {
		Message message = new MessageImpl();
		if(inputStream != null) {
			message.setContent(InputStream.class, inputStream);
		}
		
        if(contentType != null) {
        	message.put("Content-Type", contentType);
        }
        
        if(soapHeaders != null) {
	        List<Header> headers = new ArrayList<Header>();
	        if(soapHeaders) {
	        	headers.add(new SoapHeader(new QName(HEADER_LOCAL_NAME), HEADER_OBJECT, null));
	        	headers.add(new SoapHeader(new QName("other"), "", null));
	        }
	        message.put(Header.HEADER_LIST, headers);
        }
        
        Exchange e = new ExchangeImpl();
        message.setExchange(e);
        
        Endpoint endp = mock(Endpoint.class);
        e.put(Endpoint.class, endp);
        
        EndpointInfo epr = new EndpointInfo(); 
        when(endp.getEndpointInfo()).thenReturn(epr);
        
        interceptor.handleMessage(message);
	}

	protected StringBuilder fireMessageWriterOut(LoggingOutInterceptor interceptor, String xml, String contentType) throws Exception {
		if(writer) {
			fireMessageWriterOut((Interceptor<Message>)interceptor, xml, contentType);
			
			return null;
		} else {
			StringBuilder builder = new StringBuilder();
			StringWriter stringWriter = new StringWriter();
			if(xml != null) {
				stringWriter.write(xml);
			}
			interceptor.writePayload(builder, stringWriter, contentType);
			
			return builder;
		}
	}

	protected void fireMessageWriterOut(Interceptor<Message> interceptor, String xml, String contentType) throws IOException {
		
		Message message = new MessageImpl();
        message.setContent(Writer.class, new StringWriter());
        if(contentType != null) {
        	message.put("Content-Type", contentType);
        }
        
        List<Header> headers = new ArrayList<Header>();
        headers.add(new SoapHeader(new QName(HEADER_LOCAL_NAME), HEADER_OBJECT, null));
        message.put(Header.HEADER_LIST, headers);
        
        Exchange e = new ExchangeImpl();
        message.setExchange(e);
        
        Endpoint endp = mock(Endpoint.class);
        e.put(Endpoint.class, endp);
        
        EndpointInfo epr = new EndpointInfo(); 
        when(endp.getEndpointInfo()).thenReturn(epr);
        
        interceptor.handleMessage(message);
        
        Writer writer = message.getContent(Writer.class);

		if(xml != null) {
			writer.write(xml);
		}
			
        writer.close();
	}

	protected void fireMessageByteOut(Interceptor<Message> interceptor, String xml, String contentType, Boolean soapHeaders) throws IOException {
		fireMessageByteOut(interceptor, new ByteArrayOutputStream(), xml, contentType, soapHeaders);
	}

	protected void fireMessageByteOut(Interceptor<Message> interceptor, OutputStream outputStream, String xml, String contentType, Boolean soapHeaders) throws IOException {
		Message message = new MessageImpl();
        message.setContent(OutputStream.class, outputStream);
        if(contentType != null) {
        	message.put("Content-Type", contentType);
        }
        
        byte[] content = null;
        if(xml != null) {
        	content = xml.getBytes("UTF-8");
		}
        
        if(soapHeaders != null) {
	        List<Header> headers = new ArrayList<Header>();
	        if(soapHeaders) {
	        	headers.add(new SoapHeader(new QName(HEADER_LOCAL_NAME), HEADER_OBJECT, null));
	        	if(content != null) {
	        		headers.add(new SoapHeader(new QName("Content-Size"), Integer.toString(content.length), null));
	        	}
	        }
	        message.put(Header.HEADER_LIST, headers);
        }
        
        Exchange e = new ExchangeImpl();
        message.setExchange(e);
        
        Endpoint endp = mock(Endpoint.class);
        e.put(Endpoint.class, endp);
        
        EndpointInfo epr = new EndpointInfo(); 
        when(endp.getEndpointInfo()).thenReturn(epr);
        
        interceptor.handleMessage(message);
        
        if(outputStream != null) {
	        OutputStream writer = message.getContent(OutputStream.class);
	
			if(content != null) {
				writer.write(content);
			}
			
	        writer.close();
        }
	}

}
