package com.greenbird.xmlformatter.cxf.example;

import com.greenbird.xmlns.schema.logger.PerformLogMessageRequest;
import com.greenbird.xmlns.schema.logger.PerformLogMessageResponse;

public class MyBean {

    public PerformLogMessageResponse myMethod(PerformLogMessageRequest request) {

    	PerformLogMessageResponse response = new PerformLogMessageResponse();
    	response.setStatus(1);
        return response;
    }
}
