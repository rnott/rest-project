package com.trizic.api.service.v1;


public class Registration {

    private String advisorId;
    private String apiKey;

    public Registration() {}

    public Registration( String advisorId, String apiKey ) {
        this.advisorId = advisorId;
        this.apiKey = apiKey;
    }

    /**
     * Retrieve the current value of the advisorId property.
     * <p>
     * @return the current property value.
     */
    public String getAdvisorId() {
        return advisorId;
    }
    
    /**
     * Retrieve the current value of the apiKey property.
     * <p>
     * @return the current property value.
     */
    public String getApiKey() {
        return apiKey;
    }
}
