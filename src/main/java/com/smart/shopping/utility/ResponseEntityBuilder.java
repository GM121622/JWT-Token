package com.smart.shopping.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.ResponseEntity;

import com.smart.shopping.pojo.ResponseFormat;

public class ResponseEntityBuilder {

    private static final Logger logger = LogManager.getLogger(ResponseEntityBuilder.class);
	
    private ResponseFormat createResponseObject(int HTTPCode, String responseStatus, String developerMessage, String userMessage, String userMessageCode,String[] responseCodes, String[] errorCode, String moreInfo, Object data) {
    	ThreadContext.put("LEGLevel","LEG4");	
    	ResponseFormat responseFormat = new ResponseFormat();
		responseFormat.setData(data);
		responseFormat.setDeveloperMessage(developerMessage);
		responseFormat.setResponseCodes(responseCodes);
		responseFormat.setErrorCodes(errorCode);
		responseFormat.setHTTPCode(HTTPCode);
		responseFormat.setMoreInfo(moreInfo);
		responseFormat.setUserMessage(userMessage);
		responseFormat.setUserMessageCode(ThreadContext.get("correlationId"));
		responseFormat.setResponseStatus(responseStatus);
		logger.info("In ResponseEntityBuilder ",responseFormat);
		return responseFormat;
}


	public ResponseEntity<Object> createFailureResponse(int HTTPCode, String developerMessage, String userMessage, String userMessageCode, String[] errorCode, String moreInfo, Object data) {
	 
		ResponseFormat responseFormat = createResponseObject(HTTPCode, "Failure", developerMessage, userMessage, userMessageCode, null, errorCode, moreInfo, data);
		
		  return ResponseEntity.status(HTTPCode).body(responseFormat);
	}
	
	
	public ResponseEntity<Object> createFailureResponse(int HTTPCode,  String userMessage, String[] errorCode) {
	
		ResponseFormat responseFormat = createResponseObject(HTTPCode, "Failure", null, userMessage,null,null, errorCode, null, null);
		
		  return ResponseEntity.status(HTTPCode).body(responseFormat);
	}
	
	public ResponseEntity<Object> createSuccessResponse(int HTTPCode,  String userMessage, String[] tilacode, Object data) {
	
		ResponseFormat responseFormat = createResponseObject(HTTPCode, "Success", null,userMessage, null, tilacode, null,null, data);
		
		  return ResponseEntity.status(HTTPCode).body(responseFormat);
	}
	
	public ResponseEntity<Object> createExceptionResponse(int HTTPCode, String developerMessage, String userMessage, String[] errorCode, String moreInfo) {
		 
		ResponseFormat responseFormat = createResponseObject(HTTPCode, "Failure", developerMessage, userMessage, null, null, errorCode, moreInfo, null);
		logger.error("Exception:",responseFormat);
		  return ResponseEntity.status(HTTPCode).body(responseFormat);
	}
		
}

