
package com.trizic.api.service.v1;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "pageNumber",
    "pageSize",
    "numberOfPages",
    "totalNumberOfElements",
    "page"
})
public class ModelPage {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("pageNumber")
    @NotNull
    private Integer pageNumber;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("pageSize")
    @NotNull
    private Integer pageSize;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("numberOfPages")
    @NotNull
    private Integer numberOfPages;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("totalNumberOfElements")
    @NotNull
    private Integer totalNumberOfElements;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("page")
    @Valid
    @NotNull
    private List<ModelResponse> page = new ArrayList<ModelResponse>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The pageNumber
     */
    @JsonProperty("pageNumber")
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * 
     * (Required)
     * 
     * @param pageNumber
     *     The pageNumber
     */
    @JsonProperty("pageNumber")
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ModelPage withPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The pageSize
     */
    @JsonProperty("pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 
     * (Required)
     * 
     * @param pageSize
     *     The pageSize
     */
    @JsonProperty("pageSize")
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ModelPage withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The numberOfPages
     */
    @JsonProperty("numberOfPages")
    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * 
     * (Required)
     * 
     * @param numberOfPages
     *     The numberOfPages
     */
    @JsonProperty("numberOfPages")
    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public ModelPage withNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The totalNumberOfElements
     */
    @JsonProperty("totalNumberOfElements")
    public Integer getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    /**
     * 
     * (Required)
     * 
     * @param totalNumberOfElements
     *     The totalNumberOfElements
     */
    @JsonProperty("totalNumberOfElements")
    public void setTotalNumberOfElements(Integer totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public ModelPage withTotalNumberOfElements(Integer totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The page
     */
    @JsonProperty("page")
    public List<ModelResponse> getPage() {
        return page;
    }

    /**
     * 
     * (Required)
     * 
     * @param page
     *     The page
     */
    @JsonProperty("page")
    public void setPage(List<ModelResponse> page) {
        this.page = page;
    }

    public ModelPage withPage(List<ModelResponse> page) {
        this.page = page;
        return this;
    }

}
