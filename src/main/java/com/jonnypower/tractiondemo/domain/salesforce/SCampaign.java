package com.jonnypower.tractiondemo.domain.salesforce;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SCampaign extends SObject {

    // =========================
    // CONSTANTS
    // =========================

    private static final String S_OBJECT_TYPE = "Campaign";

    // =========================
    // ATTRIBUTES
    // =========================



    // =========================
    // IMPLEMENTATION OF SObject
    // =========================

    @Override
    public String getSObjectType() {
        return S_OBJECT_TYPE;
    }

}
