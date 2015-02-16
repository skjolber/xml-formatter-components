/***************************************************************************
 * Copyright 2014 greenbird Integration Technology, http://www.greenbird.com/
 *
 * This file is part of the 'xml-formatter' project available at
 * http://greenbird.github.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.greenbird.xmlformatter.cxf;

import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.greenbird.xml.prettyprinter.PrettyPrinter;


/**
 * Default XML Logging interceptor.
 */

public class XMLLoggingOutInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final String LOCAL_NAME = "MessageID";

	private static final int PROPERTIES_SIZE = 128;
	
    private String name = "<interceptor name not set>";

    protected PrettyPrinter prettyPrinter = null;
    protected Logger logger = null;
    protected Level reformatSuccessLevel;
    protected Level reformatFailureLevel;
   
    public XMLLoggingOutInterceptor() {
        this(LogUtils.getLogger(XMLLoggingOutInterceptor.class), Level.INFO,  Level.WARNING);
    }
    
    public XMLLoggingOutInterceptor(PrettyPrinter prettyPrinter) {
        this(LogUtils.getLogger(XMLLoggingOutInterceptor.class), Level.INFO,  Level.WARNING);
        
        this.prettyPrinter = prettyPrinter;
    }

    
    public XMLLoggingOutInterceptor(Logger logger, Level reformatSuccessLevel, Level reformatFailureLevel) {
    	/**
    	// Note to self: capture more of the output by being inserted EARLIER in the interceptor chain (layered system in the form of pre+post processing interceptors)
    	super(Phase.PREPARE_SEND);
        addAfter(MessageSenderInterceptor.class.getName());
		*/
    	
    	super(Phase.PRE_STREAM);
        addBefore(StaxOutInterceptor.class.getName());
        this.logger = logger;
        this.reformatSuccessLevel = reformatSuccessLevel;
        this.reformatFailureLevel = reformatFailureLevel;
    }

    public XMLLoggingOutInterceptor(PrettyPrinter prettyPrinter, Logger logger, Level reformatSuccessLevel, Level reformatFailureLevel) {
    	this(logger, reformatSuccessLevel, reformatFailureLevel);
    	
    	this.prettyPrinter = prettyPrinter;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void handleMessage(Message message) throws Fault {
    	if (!logger.isLoggable(reformatSuccessLevel)) {
 			 return;
        }

        final OutputStream os = message.getContent(OutputStream.class);
        if (os == null) {
            return;
        }

        // Write the output while caching it for the log message
        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
        message.setContent(OutputStream.class, newOut);

        int contentSize = -1;
        String contentSizeString = getHeader(message, "Content-Size");
        if(contentSizeString != null) {
        	contentSize = Integer.parseInt(contentSizeString);
        }
        if(contentSize == -1) {
            contentSize = 8 * 1024;
        }
        
        StringBuilder buffer = new StringBuilder(contentSize + PROPERTIES_SIZE);
        
        // perform local logging - to the buffer 
        buffer.append(name);
        
        logProperties(buffer, message);
        
        newOut.registerCallback(new SimpleLoggingCallback(buffer));
    }
    
	/**
     * Method intended for use within subclasses. Log custom field here.
     * 
     * @param message message
    */

    protected void logProperties(StringBuilder buffer, Message message) {
        final String messageId = getIdHeader(message);
        if(messageId != null) {
	        buffer.append(" MessageId=");
	        buffer.append(messageId);
        }
    }

    private class SimpleLoggingCallback implements CachedOutputStreamCallback {
        private StringBuilder buffer;

        public SimpleLoggingCallback(StringBuilder buffer) {
            this.buffer = buffer;;
        }

        public void onFlush(CachedOutputStream cos) {

        }

        public void onClose(CachedOutputStream cos) {

        	int length = buffer.length();
            try {
                cos.writeCacheTo(buffer);
            } catch (Exception ex) {
                // ignore
            }
            
            // decode chars from bytes
            char[] chars = new char[buffer.length() - length];
            buffer.getChars(length, buffer.length(), chars, 0);
            
            buffer.setLength(length);
            
            buffer.ensureCapacity(length + chars.length);

            // pretty print XML
            if(prettyPrinter.process(chars, 0, chars.length, buffer)) {
                // log as normal
            	logger.log(reformatSuccessLevel, buffer.toString());
            } else {
                // something unexpected - log as exception
                buffer.append(" was unable to format XML\n");
                buffer.append(chars); // unmodified XML
                
                logger.log(reformatFailureLevel, buffer.toString());
            }
        }
    }
    
    /**
     * Gets theMessageID header in the list of headers.
     *
     */
    protected String getIdHeader(Message message) {
    	return getHeader(message, LOCAL_NAME);
    }

    protected String getHeader(Message message, String name) {
        List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);

        if(headers != null) {
	        for(Header header:headers) {
	            if(header.getName().getLocalPart().equalsIgnoreCase(name)) {
	                return header.getObject().toString();
	            }
	        }
        }
        return null;
    }        

    public void setPrettyPrinter(PrettyPrinter prettyPrinter) {
		this.prettyPrinter = prettyPrinter;
	}
    
    public PrettyPrinter getPrettyPrinter() {
		return prettyPrinter;
	}
    
    public void setLogger(Logger logger) {
		this.logger = logger;
	}
    
    public Logger getLogger() {
		return logger;
	}
    
    public void setReformatFailureLevel(Level reformatFailureLevel) {
		this.reformatFailureLevel = reformatFailureLevel;
	}
    
    public Level getReformatFailureLevel() {
		return reformatFailureLevel;
	}
    
    public void setReformatSuccessLevel(Level reformatSuccessLevel) {
		this.reformatSuccessLevel = reformatSuccessLevel;
	}
    
    public Level getReformatSuccessLevel() {
		return reformatSuccessLevel;
	}
    
    public String getName() {
		return name;
	}
}
