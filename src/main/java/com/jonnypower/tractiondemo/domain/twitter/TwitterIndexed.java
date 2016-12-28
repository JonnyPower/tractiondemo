package com.jonnypower.tractiondemo.domain.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class TwitterIndexed {

    // =========================
    // ATTRIBUTES
    // =========================

    @JsonProperty("id_str")
    private String id;


    // =========================
    // PUBLIC METHODS
    // =========================

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
