package com.jonnypower.tractiondemo.domain.salesforce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SObject {

    // =========================
    // ATTRIBUTES
    // =========================

    @JsonProperty(value="Id")
    private String id;

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "CreatedBy")
    private SUser createdBy;

    @JsonProperty(value = "LastModifiedBy")
    private SUser lastModifiedBy;

    // =========================
    // PUBLIC METHODS
    // =========================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SUser createdBy) {
        this.createdBy = createdBy;
    }

    public SUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(SUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    // =========================
    // ABSTRACT METHODS
    // =========================

    @JsonIgnore
    public abstract String getSObjectType();

}
