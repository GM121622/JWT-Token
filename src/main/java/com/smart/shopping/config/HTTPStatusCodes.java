package com.smart.shopping.config;

public enum HTTPStatusCodes {

	BAD_REQUEST(400),
	OK(200),
	CREATED(201),	
	FORBIDDEN(403),
	INTERNAL_SERVER_ERROR(500),
	METHOD_NOT_ALLOWED(405),
	CONFLICT(409),
	NOT_FOUND(404),
	NOT_ACCEPTABLE(406),
	UNAUTHORIZED(401),
	UNSUPPORTED_MEDIA_TYPE (415);

		 
    private int HTTPcode;
 
	HTTPStatusCodes(int code) {
        this.HTTPcode = code;
    }
 
    public int getHTTPCode() {
        return this.HTTPcode;
    }


}
