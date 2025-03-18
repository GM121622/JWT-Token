package com.smart.shopping.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseFormat {
		private int HTTPCode;
		private String responseStatus;
		private String developerMessage;
		private String userMessage;
		private String userMessageCode;
		private String[] responseCodes;
		private String[] errorCodes;
		private String moreInfo;
		private Object data;	
}
