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

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;

import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.staxutils.StaxUtils;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;

/**
 * This file was derived from its superclasses.
 */
@NoJSR250Annotations
public class StAXLoggingOutInterceptor extends org.apache.cxf.interceptor.LoggingOutInterceptor {

	public StAXLoggingOutInterceptor() {
		super();
	}

	public StAXLoggingOutInterceptor(int lim) {
		super(lim);
	}

	public StAXLoggingOutInterceptor(PrintWriter w) {
		super(w);
	}

	public StAXLoggingOutInterceptor(String phase) {
		super(phase);
	}
	
	@Override
	protected void writePayload(StringBuilder builder, CachedOutputStream cos, String encoding, String contentType) throws Exception {
        // Just transform the XML message when the cos has content
        if (isPrettyLogging() && (contentType != null && contentType.indexOf("xml") >= 0 
            && contentType.toLowerCase().indexOf("multipart/related") < 0) && cos.size() > 0) {

            StringWriter swriter = new StringWriter();
            XMLStreamWriter xwriter = StaxUtils.createXMLStreamWriter(swriter);
            xwriter = new PrettyPrintXMLStreamWriter(xwriter, 2);
            InputStream in = cos.getInputStream();
            try {
                StaxUtils.copy(new StreamSource(in), xwriter);
            } catch (XMLStreamException xse) {
                //ignore
            } finally {
                try {
                    xwriter.flush();
                    xwriter.close();
                } catch (XMLStreamException xse2) {
                    //ignore
                }
                in.close();
            }
            
            String result = swriter.toString();
            if (result.length() < limit || limit == -1) {
                builder.append(swriter.toString());
            } else {
                builder.append(swriter.toString().substring(0, limit));
            }

        } else {
            if (StringUtils.isEmpty(encoding)) {
                cos.writeCacheTo(builder, limit);
            } else {
                cos.writeCacheTo(builder, encoding, limit);
            }
        }		
	}
 

}