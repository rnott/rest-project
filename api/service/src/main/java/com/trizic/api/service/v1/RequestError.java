package com.trizic.api.service.v1;


public class RequestError {

    private String errorCode;

    public RequestError() {}

    public RequestError( String errorCode ) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
