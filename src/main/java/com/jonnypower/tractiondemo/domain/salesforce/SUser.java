package com.jonnypower.tractiondemo.domain.salesforce;

public class SUser extends SObject {

    // =========================
    // CONSTANTS
    // =========================

    private static final String S_OBJECT_TYPE = "User";

    // =========================
    // IMPLEMENTATION OF SObject
    // =========================

    @Override
    public String getSObjectType() {
        return S_OBJECT_TYPE;
    }
}
