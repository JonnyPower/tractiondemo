package com.jonnypower.tractiondemo.domain.salesforce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.net.URL;

public class SDonation extends SObject {

    // =========================
    // CONSTANTS
    // =========================

    private static final String S_OBJECT_TYPE = "Donation__c";

    // =========================
    // ATTRIBUTES
    // =========================

    @JsonIgnore
    private SCampaign campaign;

    @JsonProperty(value = "Social_Handle__c")
    private String handle;

    @JsonProperty(value = "Social_Network__c")
    private SocialNetwork socialNetwork;

    @JsonProperty(value = "Value__c")
    private BigDecimal value;

    @JsonProperty(value = "Social_Seed__c")
    private URL socialSeed;

    // =========================
    // PUBLIC METHODS
    // =========================

    public SCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(SCampaign campaign) {
        this.campaign = campaign;
    }

    @JsonProperty(value = "Campaign__c")
    public String getCampaignId() {
        return campaign.getId();
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public SocialNetwork getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public URL getSocialSeed() {
        return socialSeed;
    }

    public void setSocialSeed(URL socialSeed) {
        this.socialSeed = socialSeed;
    }

    // =========================
    // IMPLEMENTATION OF SObject
    // =========================

    @Override
    public String getSObjectType() {
        return S_OBJECT_TYPE;
    }

}
