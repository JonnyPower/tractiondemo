package com.jonnypower.tractiondemo.service;

import com.force.api.*;
import com.jonnypower.tractiondemo.domain.salesforce.SCampaign;
import com.jonnypower.tractiondemo.domain.salesforce.SDonation;
import com.jonnypower.tractiondemo.domain.salesforce.SocialNetwork;
import com.jonnypower.tractiondemo.exception.SalesforceOAuthSessionNotSetupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class SalesforceService {

    // =========================
    // ATTRIBUTES
    // =========================

    @Value("${salesforce.client.id}")
    private String clientId;

    @Value("${salesforce.client.secret}")
    private String clientSecret;

    @Value("${salesforce.redirect}")
    private String redirectURI;

    @Value("#{'${salesforce.campaign.whitelist}'.split(',')}")
    private List<String> campaignWhitelist;

    private String oAuthRequestUrl;

    private ApiConfig apiConfig;
    private ForceApi forceApi;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // =========================
    // SETUP
    // =========================

    @PostConstruct
    public void setup() {

        apiConfig = new ApiConfig()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectURI);

        oAuthRequestUrl = Auth.startOAuthWebServerFlow(new AuthorizationRequest().apiConfig(apiConfig));
        logger.info("OAUTH: "+oAuthRequestUrl);
    }

    // =========================
    // PUBLIC METHODS
    // =========================

    public String getOAuthRequestUrl() {
        return oAuthRequestUrl;
    }

    public String registerDonation(String campaignName, String socialHandle, String seedId, int amount) {
        if(forceApi == null) {
            throw new SalesforceOAuthSessionNotSetupException();
        }
        final Optional<SCampaign> campaignOptional = findCampaign(campaignName);
        final SDonation donation = new SDonation();
        if(campaignOptional.isPresent()) {
            donation.setCampaign(campaignOptional.get());
        }
        donation.setHandle(socialHandle);
        donation.setSocialNetwork(SocialNetwork.TWITTER);
        donation.setValue(new BigDecimal((double) amount / 100d));
        try {
            donation.setSocialSeed(new URL(String.format("https://twitter.com/%s/status/%s", socialHandle, seedId)));
        } catch (MalformedURLException ex) {
            logger.error("Malformed url for social seed (twitter status)", ex);
        }
        return saveDonation(donation);
    }

    public boolean completeOAuth(String accessToken) {
        try {
            final ApiSession apiSession = Auth.completeOAuthWebServerFlow(new AuthorizationResponse()
                    .apiConfig(apiConfig)
                    .code(accessToken));
            final ForceApi api = new ForceApi(apiConfig, apiSession);
            forceApi = api;
            return true;
        } catch (AuthException ex) {
            logger.error("Failed to complete OAuth", ex);
            return false;
        }
    }

    // =========================
    // PRIVATE METHODS
    // =========================

    public Optional<SCampaign> findCampaign(String name) {
        if(campaignWhitelist.contains(name)) {
            final List<SCampaign> results =  forceApi.query("select id,name from Campaign where name = '" + name + "'", SCampaign.class).getRecords();
            if(results.size() > 0) {
                return Optional.of(results.get(0));
            }
        }
        return Optional.empty();
    }

    public String saveDonation(SDonation donation) {
        final String createdId = forceApi.createSObject(donation.getSObjectType(), donation);
        logger.info("Created " + donation.getSObjectType() + " with id " + createdId);
        return createdId;
    }

}
