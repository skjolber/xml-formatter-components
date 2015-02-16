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

package com.greenbird.xmlformatter.mule.cxf;

import java.util.logging.Level;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.message.Message;
import org.mule.api.MuleEvent;
import org.mule.module.cxf.CxfConstants;

import com.greenbird.xml.prettyprinter.PrettyPrinter;


/**
 * Default implementation of out-interceptor. 
 */

public class XMLLoggingOutInterceptor extends com.greenbird.xmlformatter.cxf.XMLLoggingOutInterceptor  {

    public XMLLoggingOutInterceptor() {
        super(LogUtils.getLogger(XMLLoggingOutInterceptor.class), Level.INFO,  Level.WARNING);
    }
    
    public XMLLoggingOutInterceptor(PrettyPrinter prettyPrinter) {
    	this();
        
        this.prettyPrinter = prettyPrinter;
    }

	public void logProperties(StringBuilder buffer, Message message) {
        MuleEvent event = (MuleEvent) message.getExchange().get(CxfConstants.MULE_EVENT);

        final String uniqueId = event.getMessage().getUniqueId();
        String correlationId = event.getMessage().getCorrelationId();

        buffer.append(" uniqueId=");
        buffer.append(uniqueId);
        buffer.append(" correlationId=");
        buffer.append(correlationId);

    }
}
