package com.jonnypower.tractiondemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonnypower.tractiondemo.domain.twitter.TwitterMessage;
import com.jonnypower.tractiondemo.domain.twitter.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TwitterMessageUtils {

    // =========================
    // ATTRIBUTES
    // =========================

    private final static Logger logger = LoggerFactory.getLogger(TwitterMessageUtils.class);

    // =========================
    // PUBLIC METHODS
    // =========================

    public static Optional<TwitterMessage> getTwitterMessage(ObjectMapper objectMapper, JsonNode jsonNode) {
        if(jsonNode.has("text") && jsonNode.has("user")) {
            try {
                final TwitterStatus twitterStatus = objectMapper.treeToValue(jsonNode, TwitterStatus.class);
                return Optional.of(twitterStatus);
            } catch (JsonProcessingException ex) {
                logger.error("Failed to parse twitterStatus", ex);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
