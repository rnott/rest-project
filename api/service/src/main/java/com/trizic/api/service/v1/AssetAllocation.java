
package com.trizic.api.service.v1;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "symbol",
    "percentage"
})
public class AssetAllocation {

    /**
     * The asset symbol
     * (Required)
     * 
     */
    @JsonProperty("symbol")
    @NotNull
    private String symbol;
    /**
     * The percentage of the model to allocate for the symbol
     * (Required)
     * 
     */
    @JsonProperty("percentage")
    @NotNull
    private Double percentage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The asset symbol
     * (Required)
     * 
     * @return
     *     The symbol
     */
    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    /**
     * The asset symbol
     * (Required)
     * 
     * @param symbol
     *     The symbol
     */
    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public AssetAllocation withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * The percentage of the model to allocate for the symbol
     * (Required)
     * 
     * @return
     *     The percentage
     */
    @JsonProperty("percentage")
    public Double getPercentage() {
        return percentage;
    }

    /**
     * The percentage of the model to allocate for the symbol
     * (Required)
     * 
     * @param percentage
     *     The percentage
     */
    @JsonProperty("percentage")
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public AssetAllocation withPercentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public AssetAllocation withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
