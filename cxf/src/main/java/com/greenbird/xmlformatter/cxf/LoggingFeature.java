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

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Logging;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

import com.greenbird.xml.prettyprinter.PrettyPrinter;
import com.greenbird.xml.prettyprinter.PrettyPrinterFactory;
import com.greenbird.xml.prettyprinter.plain.PlainPrettyPrinter;

/**
 * This class is used to control message-on-the-wire logging. 
 * By attaching this feature to an endpoint, you
 * can specify logging. If this feature is present, an endpoint will log input
 * and output of ordinary and log messages.
 * <pre>
    &lt;jaxws:endpoint ...&gt;
      &lt;jaxws:features&gt;
       &lt;bean class="org.greenbird.xmlformatter.cxf.LoggingFeature"/&gt;
      &lt;/jaxws:features&gt;
    &lt;/jaxws:endpoint&gt;
  </pre>
  This file was derived from {@linkplain org.apache.cxf.feature.LoggingFeature}}.
 */
@NoJSR250Annotations
public class LoggingFeature extends AbstractFeature {
    private static final int DEFAULT_LIMIT = 64 * 1024;
    
    private static final LoggingInInterceptor IN = new LoggingInInterceptor(DEFAULT_LIMIT, new PlainPrettyPrinter(false));
    private static final LoggingOutInterceptor OUT = new LoggingOutInterceptor(DEFAULT_LIMIT, new PlainPrettyPrinter(false));

    private String inLocation;
    private String outLocation;
    private boolean prettyLogging;
    private boolean showBinary;
    
    /** limit on the sum of all XML data */
    private int limit = DEFAULT_LIMIT;
    
    private PrettyPrinterFactory factory = new PrettyPrinterFactory();

    public LoggingFeature() {
    }
    
    public LoggingFeature(int lim) {
        limit = lim;
    }
    public LoggingFeature(String in, String out) {
        inLocation = in;
        outLocation = out;
    }
    public LoggingFeature(String in, String out, int lim) {
        inLocation = in;
        outLocation = out;
        limit = lim;
    }

    public LoggingFeature(String in, String out, int lim, boolean p) {
        inLocation = in;
        outLocation = out;
        limit = lim;
        prettyLogging = p;
    }
    
    public LoggingFeature(String in, String out, int lim, boolean p, boolean showBinary) {
        this(in, out, lim, p);
        this.showBinary = showBinary;
    }

    public LoggingFeature(Logging annotation) {
        inLocation = annotation.inLocation();
        outLocation = annotation.outLocation();
        limit = annotation.limit();
        prettyLogging = annotation.pretty();
        //showBinary = annotation.showBinary();
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        if (limit == DEFAULT_LIMIT 
        		&& inLocation == null 
        		&& outLocation == null 
        		&& !prettyLogging) {
            provider.getInInterceptors().add(IN);
            provider.getInFaultInterceptors().add(IN);
            provider.getOutInterceptors().add(OUT);
            provider.getOutFaultInterceptors().add(OUT);
        } else {
        	PrettyPrinter newPrettyPrinter = factory.newPrettyPrinter();
            LoggingInInterceptor in = new LoggingInInterceptor(limit, newPrettyPrinter);
            in.setOutputLocation(inLocation);
            in.setPrettyLogging(prettyLogging);
            //in.setShowBinaryContent(showBinary);
            LoggingOutInterceptor out = new LoggingOutInterceptor(limit, newPrettyPrinter);
            out.setOutputLocation(outLocation);
            out.setPrettyLogging(prettyLogging);
            //out.setShowBinaryContent(showBinary);
            
            provider.getInInterceptors().add(in);
            provider.getInFaultInterceptors().add(in);
            provider.getOutInterceptors().add(out);
            provider.getOutFaultInterceptors().add(out);
        }
    }

    public void setLimit(int lim) {
        limit = lim;
    }
    
    /**
     * Retrieve the value set with {@link #setLimit(int)}.
     * 
     * @return limit
     */
    public int getLimit() {
        return limit;
    }
    
    /**
     */
    public boolean isPrettyLogging() {
        return prettyLogging;
    }
    /**
     * Turn pretty logging of XML content on/off
     * @param prettyLogging true if XML is to be reformatted
     */
    public void setPrettyLogging(boolean prettyLogging) {
        this.prettyLogging = prettyLogging;
    }

	public boolean isRobustness() {
		return factory.isRobustness();
	}

	public void setRobustness(boolean robustness) {
		factory.setRobustness(robustness);
	}

	public boolean isIgnoreWhitespace() {
		return factory.isIgnoreWhitespace();
	}

	public void setIgnoreWhitespace(boolean ignoreWhitespace) {
		factory.setIgnoreWhitespace(ignoreWhitespace);
	}

	public boolean isPrettyPrintCData() {
		return factory.isPrettyPrintCData();
	}

	public void setPrettyPrintCData(boolean prettyPrintCData) {
		factory.setPrettyPrintCData(prettyPrintCData);
	}

	public boolean isPrettyPrintComments() {
		return factory.isPrettyPrintComments();
	}

	public void setPrettyPrintComments(boolean prettyPrintComments) {
		factory.setPrettyPrintComments(prettyPrintComments);
	}

	public boolean isXmlDeclaration() {
		return factory.isXmlDeclaration();
	}

	public void setXmlDeclaration(boolean xmlDeclaration) {
		factory.setXmlDeclaration(xmlDeclaration);
	}

	public int getMaxTextNodeLength() {
		return factory.getMaxTextNodeLength();
	}

	public void setMaxTextNodeLength(int maxTextNodeLength) {
		factory.setMaxTextNodeLength(maxTextNodeLength);
	}

	public int getMaxCDATANodeLength() {
		return factory.getMaxCDATANodeLength();
	}

	public void setMaxCDATANodeLength(int maxCDATANodeLength) {
		factory.setMaxCDATANodeLength(maxCDATANodeLength);
	}

	public List<String> getAnonymizeFilters() {
		String[] filters = factory.getAnonymizeFilters();
		
		List<String> list = new ArrayList<String>();
		if(filters != null) {
			for(String filter : filters) {
				list.add(filter);
			}
		}
		return list;
	}

	public void setAnonymizeFilters(List<String> anonymizeFilters) {
		if(anonymizeFilters != null) {
			factory.setAnonymizeFilters(anonymizeFilters.toArray(new String[anonymizeFilters.size()]));
		} else {
			factory.setAnonymizeFilters(null);
		}
	}

	public List<String> getPruneFilters() {
		String[] filters = factory.getPruneFilters();
		
		List<String> list = new ArrayList<String>();
		if(filters != null) {
			for(String filter : filters) {
				list.add(filter);
			}
		}
		return list;
	}

	public void setPruneFilters(List<String> pruneFilters) {
		if(pruneFilters != null) {
			factory.setPruneFilters(pruneFilters.toArray(new String[pruneFilters.size()]));
		} else {
			factory.setPruneFilters(null);
		}
	}    
	
	public void setIndentationMultiplier(int indentationMultiplier) {
		factory.setIndentationMultiplier(indentationMultiplier);
	}
	
	public int getIndentationMultiplier() {
		return factory.getIndentationMultiplier();
	}

	public void setIndentationCharacter(char indentationCharacter) {
		factory.setIndentationCharacter(indentationCharacter);
	}
	
	public char getIndentationCharacter() {
		return factory.getIndentationCharacter();
	}
    
    
}