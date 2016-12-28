package com.jonnypower.tractiondemo.domain.salesforce;

public enum SocialNetwork {
    TWITTER("Twitter");

    private final String value;

    SocialNetwork(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
