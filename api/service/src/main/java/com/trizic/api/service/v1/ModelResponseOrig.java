
package com.trizic.api.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "guid",
    "name",
    "description",
    "cashHoldingPercentage",
    "driftPercentage",
    "createdOn",
    "modelType",
    "rebalanceFrequency",
    "advisorId",
    "assetAllocations"
})
public class ModelResponseOrig {

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
    private Integer cashHoldingPercentage;
    /**
     * Percentage of drift from target allocation of assets
     * (Required)
     * 
     */
    @JsonProperty("driftPercentage")
    @NotNull
    private Integer driftPercentage;
    /**
     * Date model created
     * 
     */
    @JsonProperty("createdOn")
    private String createdOn;
    /**
     * Type of model
     * (Required)
     * 
     */
    @JsonProperty("modelType")
    @NotNull
    private ModelResponseOrig.ModelType modelType;
    /**
     * Frequency to rebalance model
     * (Required)
     * 
     */
    @JsonProperty("rebalanceFrequency")
    @NotNull
    private ModelResponseOrig.RebalanceFrequency rebalanceFrequency;
    /**
     * Advisor who manages the model, guid
     * (Required)
     * 
     */
    @JsonProperty("advisorId")
    @NotNull
    private String advisorId;
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

    public ModelResponseOrig withGuid(String guid) {
        this.guid = guid;
        return this;
    }

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

    public ModelResponseOrig withName(String name) {
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

    public ModelResponseOrig withDescription(String description) {
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

    public ModelResponseOrig withCashHoldingPercentage(Integer cashHoldingPercentage) {
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

    public ModelResponseOrig withDriftPercentage(Integer driftPercentage) {
        this.driftPercentage = driftPercentage;
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

    public ModelResponseOrig withCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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
    public ModelResponseOrig.ModelType getModelType() {
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
    public void setModelType(ModelResponseOrig.ModelType modelType) {
        this.modelType = modelType;
    }

    public ModelResponseOrig withModelType(ModelResponseOrig.ModelType modelType) {
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
    public ModelResponseOrig.RebalanceFrequency getRebalanceFrequency() {
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
    public void setRebalanceFrequency(ModelResponseOrig.RebalanceFrequency rebalanceFrequency) {
        this.rebalanceFrequency = rebalanceFrequency;
    }

    public ModelResponseOrig withRebalanceFrequency(ModelResponseOrig.RebalanceFrequency rebalanceFrequency) {
        this.rebalanceFrequency = rebalanceFrequency;
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

    public ModelResponseOrig withAdvisorId(String advisorId) {
        this.advisorId = advisorId;
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

    public ModelResponseOrig withAssetAllocations(List<AssetAllocation> assetAllocations) {
        this.assetAllocations = assetAllocations;
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum ModelType {

        QUALIFIED("QUALIFIED"),
        TAXABLE("TAXABLE");
        private final String value;
        private final static Map<String, ModelResponseOrig.ModelType> CONSTANTS = new HashMap<String, ModelResponseOrig.ModelType>();

        static {
            for (ModelResponseOrig.ModelType c: values()) {
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
        public static ModelResponseOrig.ModelType fromValue(String value) {
            ModelResponseOrig.ModelType constant = CONSTANTS.get(value);
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
        private final static Map<String, ModelResponseOrig.RebalanceFrequency> CONSTANTS = new HashMap<String, ModelResponseOrig.RebalanceFrequency>();

        static {
            for (ModelResponseOrig.RebalanceFrequency c: values()) {
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
        public static ModelResponseOrig.RebalanceFrequency fromValue(String value) {
            ModelResponseOrig.RebalanceFrequency constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
