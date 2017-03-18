
package com.trizic.api.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "cashHoldingPercentage",
    "driftPercentage",
    "modelType",
    "rebalanceFrequency",
    "assetAllocations"
})
public class Model {

    /**
     * Unique Name for the model
     * (Required)
     * 
     */
    @JsonProperty("name")
    @NotNull
    private String name;
    /**
     * Description of the model
     * (Required)
     * 
     */
    @JsonProperty("description")
    @NotNull
    private String description;
    /**
     * Percentage of cash to hold in the model
     * (Required)
     * 
     */
    @JsonProperty("cashHoldingPercentage")
    @NotNull
    @Min(0)
    @Max(100)
    private Integer cashHoldingPercentage;
    /**
     * Percentage of drift from target allocation of assets
     * (Required)
     * 
     */
    @JsonProperty("driftPercentage")
    @NotNull
    @Min(0)
    @Max(100)
    private Integer driftPercentage;
    /**
     * Type of model
     * (Required)
     * 
     */
    @JsonProperty("modelType")
    @NotNull
    private Model.ModelType modelType;
    /**
     * Frequency to rebalance model
     * (Required)
     * 
     */
    @JsonProperty("rebalanceFrequency")
    @NotNull
    private Model.RebalanceFrequency rebalanceFrequency;
    /**
     * Asset Allocations
     * (Required)
     * 
     */
    @JsonProperty("assetAllocations")
    @Valid
    @NotNull
    private List<AssetAllocation> assetAllocations = new ArrayList<AssetAllocation>();

    /**
     * Unique Name for the model
     * (Required)
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Unique Name for the model
     * (Required)
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Model withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Description of the model
     * (Required)
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description of the model
     * (Required)
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Model withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Percentage of cash to hold in the model
     * (Required)
     * 
     * @return
     *     The cashHoldingPercentage
     */
    @JsonProperty("cashHoldingPercentage")
    public Integer getCashHoldingPercentage() {
        return cashHoldingPercentage;
    }

    /**
     * Percentage of cash to hold in the model
     * (Required)
     * 
     * @param cashHoldingPercentage
     *     The cashHoldingPercentage
     */
    @JsonProperty("cashHoldingPercentage")
    public void setCashHoldingPercentage(Integer cashHoldingPercentage) {
        this.cashHoldingPercentage = cashHoldingPercentage;
    }

    public Model withCashHoldingPercentage(Integer cashHoldingPercentage) {
        this.cashHoldingPercentage = cashHoldingPercentage;
        return this;
    }

    /**
     * Percentage of drift from target allocation of assets
     * (Required)
     * 
     * @return
     *     The driftPercentage
     */
    @JsonProperty("driftPercentage")
    public Integer getDriftPercentage() {
        return driftPercentage;
    }

    /**
     * Percentage of drift from target allocation of assets
     * (Required)
     * 
     * @param driftPercentage
     *     The driftPercentage
     */
    @JsonProperty("driftPercentage")
    public void setDriftPercentage(Integer driftPercentage) {
        this.driftPercentage = driftPercentage;
    }

    public Model withDriftPercentage(Integer driftPercentage) {
        this.driftPercentage = driftPercentage;
        return this;
    }

    /**
     * Type of model
     * (Required)
     * 
     * @return
     *     The modelType
     */
    @JsonProperty("modelType")
    public Model.ModelType getModelType() {
        return modelType;
    }

    /**
     * Type of model
     * (Required)
     * 
     * @param modelType
     *     The modelType
     */
    @JsonProperty("modelType")
    public void setModelType(Model.ModelType modelType) {
        this.modelType = modelType;
    }

    public Model withModelType(Model.ModelType modelType) {
        this.modelType = modelType;
        return this;
    }

    /**
     * Frequency to rebalance model
     * (Required)
     * 
     * @return
     *     The rebalanceFrequency
     */
    @JsonProperty("rebalanceFrequency")
    public Model.RebalanceFrequency getRebalanceFrequency() {
        return rebalanceFrequency;
    }

    /**
     * Frequency to rebalance model
     * (Required)
     * 
     * @param rebalanceFrequency
     *     The rebalanceFrequency
     */
    @JsonProperty("rebalanceFrequency")
    public void setRebalanceFrequency(Model.RebalanceFrequency rebalanceFrequency) {
        this.rebalanceFrequency = rebalanceFrequency;
    }

    public Model withRebalanceFrequency(Model.RebalanceFrequency rebalanceFrequency) {
        this.rebalanceFrequency = rebalanceFrequency;
        return this;
    }

    /**
     * Asset Allocations
     * (Required)
     * 
     * @return
     *     The assetAllocations
     */
    @JsonProperty("assetAllocations")
    public List<AssetAllocation> getAssetAllocations() {
        return assetAllocations;
    }

    /**
     * Asset Allocations
     * (Required)
     * 
     * @param assetAllocations
     *     The assetAllocations
     */
    @JsonProperty("assetAllocations")
    public void setAssetAllocations(List<AssetAllocation> assetAllocations) {
        this.assetAllocations = assetAllocations;
    }

    public Model withAssetAllocations(List<AssetAllocation> assetAllocations) {
        this.assetAllocations = assetAllocations;
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum ModelType {

        QUALIFIED("QUALIFIED"),
        TAXABLE("TAXABLE");
        private final String value;
        private final static Map<String, Model.ModelType> CONSTANTS = new HashMap<String, Model.ModelType>();

        static {
            for (Model.ModelType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private ModelType(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Model.ModelType fromValue(String value) {
            Model.ModelType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public static enum RebalanceFrequency {

        MONTHLY("MONTHLY"),
        QUARTERLY("QUARTERLY"),
        SEMI_ANNUAL("SEMI_ANNUAL"),
        ANNUAL("ANNUAL");
        private final String value;
        private final static Map<String, Model.RebalanceFrequency> CONSTANTS = new HashMap<String, Model.RebalanceFrequency>();

        static {
            for (Model.RebalanceFrequency c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private RebalanceFrequency(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Model.RebalanceFrequency fromValue(String value) {
            Model.RebalanceFrequency constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
