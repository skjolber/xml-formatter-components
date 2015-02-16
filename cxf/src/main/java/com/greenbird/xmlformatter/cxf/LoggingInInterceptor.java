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

import java.util.logging.Logger;

import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.io.CachedOutputStream;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;

/**
 * This file was derived from its superclasses.
 */
@NoJSR250Annotations
public class LoggingInInterceptor extends org.apache.cxf.interceptor.LoggingInInterceptor {
	
    private static final Logger defaultLogger = LogUtils.getLogger(LoggingInInterceptor.class);
    
	private PrettyPrinter prettyPrinter;
	private Logger logger;
	
	public LoggingInInterceptor() {
		this(new PlainPrettyPrinter(false), defaultLogger);
	}
	
	public LoggingInInterceptor(PrettyPrinter prettyPrinter) {
		this(prettyPrinter, defaultLogger);
	}

	public LoggingInInterceptor(PrettyPrinter prettyPrinter, Logger logger) {
		this.prettyPrinter = prettyPrinter;
		this.logger = logger;
	}

	public LoggingInInterceptor(int limit, PrettyPrinter prettyPrinter) {
		this(limit, prettyPrinter, defaultLogger);
	}

	public LoggingInInterceptor(int limit, PrettyPrinter prettyPrinter, Logger logger) {
		super(limit);
		this.prettyPrinter = prettyPrinter;
		this.logger = logger;
	}

    protected void writePayload(StringBuilder builder, CachedOutputStream cos,
    		String encoding, String contentType) 
    				throws Exception {
    	// Just transform the XML message when the cos has content
    	if (isPrettyLogging() 
    			&& (contentType != null 
    			&& contentType.indexOf("xml") >= 0 
    			&& contentType.toLowerCase().indexOf("multipart/related") < 0) 
    			&& cos.size() > 0) {

    		int builderLength = builder.length();
    		if(encoding != null) {
    			cos.writeCacheTo(builder, encoding);
    		} else {
    			cos.writeCacheTo(builder);
    		}

    		char[] chars = new char[builder.length() - builderLength];
    		builder.getChars(builderLength, chars.length, chars, 0);
    		
    		builder.setLength(builderLength);
    		
    		if(!prettyPrinter.process(chars, 0, chars.length, builder)) {
    			// append raw XML
    			builder.append(chars);
    		}
    		
    		// limit sum of all XML data
    		if(limit != -1 && builder.length() - builderLength > limit) {
    			builder.setLength(builderLength + limit);
    		}
    	} else {
    		if (StringUtils.isEmpty(encoding)) {
    			cos.writeCacheTo(builder, limit);
    		} else {
    			cos.writeCacheTo(builder, encoding, limit);
    		}

    	}
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    public void setLogger(Logger logger) {
		this.logger = logger;
	}
    
    public void setPrettyPrinter(PrettyPrinter prettyPrinter) {
		this.prettyPrinter = prettyPrinter;
	}
    
    public PrettyPrinter getPrettyPrinter() {
		return prettyPrinter;
	}
    
}