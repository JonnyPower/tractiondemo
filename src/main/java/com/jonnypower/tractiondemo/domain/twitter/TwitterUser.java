package com.jonnypower.tractiondemo.domain.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitterUser extends TwitterIndexed {

    // =========================
    // ATTRIBUTES
    // =========================

    @JsonProperty("screen_name")
    private String screenName;

    // =========================
    // PUBLIC METHODS
    // =========================

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }

}
