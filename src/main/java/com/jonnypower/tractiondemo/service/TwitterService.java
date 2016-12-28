package com.jonnypower.tractiondemo.service;

import com.jonnypower.tractiondemo.domain.twitter.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class TwitterService {

    // =========================
    // ATTRIBUTES
    // =========================

    @Value("${twitter.rest.app.key}")
    private String appKey;

    @Value("${twitter.rest.app.secret}")
    private String appSecret;

    @Value("${twitter.rest.access.token}")
    private String accessToken;

    @Value("${twitter.rest.access.secret}")
    private String accessSecret;

    @Value("${twitter.rest.reply_status}")
    private String replyStatusTextFormat;

    @Value("${donate.url}")
    private String donateUrlBase;

    @Value("${donate.campaign}")
    private String donateCampaign;

    private Twitter twitter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // =========================
    // SETUP
    // =========================

    @PostConstruct
    public void setup() {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(appKey)
                .setOAuthConsumerSecret(appSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessSecret);
        final TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    // =========================
    // PUBLIC METHODS
    // =========================

    public void replyWithPlug(TwitterStatus replyTo) {
        final String statusText = String.format(replyStatusTextFormat, "@"+replyTo.getUser().getScreenName(), donateUrlWithParameters(replyTo));
        try {
            twitter.updateStatus(statusText);
        } catch (TwitterException ex) {
            logger.error("Failed to plug with reply status", ex);
        }
    }

    // =========================
    // PRIVATE METHODS
    // =========================

    private String donateUrlWithParameters(TwitterStatus status) {
        StringBuilder stringBuilder = new StringBuilder(donateUrlBase);
        stringBuilder.append("?");
        try {
            stringBuilder.append("c=" + URLEncoder.encode(donateCampaign, "UTF-8"));
            stringBuilder.append("&");
            stringBuilder.append("h=" + URLEncoder.encode(status.getUser().getScreenName(), "UTF-8"));
            stringBuilder.append("&");
            stringBuilder.append("s=" + URLEncoder.encode(status.getId(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            logger.error("Couldn't encode for url parameters", ex);
        }
        return stringBuilder.toString();
    }

}
