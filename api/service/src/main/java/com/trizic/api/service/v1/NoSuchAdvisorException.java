package com.trizic.api.service.v1;


public class NoSuchAdvisorException extends RuntimeException {

    private static final long serialVersionUID = 1667578517510121739L;
    private static final String ERROR_CODE = "advisor.not.found";

    private final String advisorId;

    public NoSuchAdvisorException( String advisorId ) {
        super( ERROR_CODE );
        this.advisorId = advisorId;
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }

    public String getAdvisorId() {
        return advisorId;
    }
}
