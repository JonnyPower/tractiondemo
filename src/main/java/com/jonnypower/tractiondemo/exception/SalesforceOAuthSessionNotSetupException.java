package com.jonnypower.tractiondemo.exception;

public class SalesforceOAuthSessionNotSetupException extends RuntimeException {

    public SalesforceOAuthSessionNotSetupException() {
        super("Salesforce OAuthSession not setup, authenticate at /salesforce/request with correct secret GET parameter");
    }

}
