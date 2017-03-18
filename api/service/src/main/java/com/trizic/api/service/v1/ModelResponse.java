
package com.trizic.api.service.v1;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "guid",
    "createdOn",
    "advisorId",
})
public class ModelResponse extends Model {

    /**
     * guid for model
     * (Required)
     * 
     */
    @JsonProperty("guid")
    @NotNull
    private String guid;
    /**
     * Unique Name for the model
     * (Required)
     * 
     */
    /**
     * Date model created
     * 
     */
    @JsonProperty("createdOn")
    private String createdOn;
    /**
     * Advisor who manages the model, guid
     * (Required)
     * 
     */
    @JsonProperty("advisorId")
    @NotNull
    private String advisorId;

    /**
     * guid for model
     * (Required)
     * 
     * @return
     *     The guid
     */
    @JsonProperty("guid")
    public String getGuid() {
        return guid;
    }

    /**
     * guid for model
     * (Required)
     * 
     * @param guid
     *     The guid
     */
    @JsonProperty("guid")
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public ModelResponse withGuid(String guid) {
        this.guid = guid;
        return this;
    }

    /**
     * Date model created
     * 
     * @return
     *     The createdOn
     */
    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Date model created
     * 
     * @param createdOn
     *     The createdOn
     */
    @JsonProperty("createdOn")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public ModelResponse withCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    /**
     * Advisor who manages the model, guid
     * (Required)
     * 
     * @return
     *     The advisorId
     */
    @JsonProperty("advisorId")
    public String getAdvisorId() {
        return advisorId;
    }

    /**
     * Advisor who manages the model, guid
     * (Required)
     * 
     * @param advisorId
     *     The advisorId
     */
    @JsonProperty("advisorId")
    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public ModelResponse withAdvisorId(String advisorId) {
        this.advisorId = advisorId;
        return this;
    }
}
