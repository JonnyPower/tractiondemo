package com.jonnypower.tractiondemo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonnypower.tractiondemo.domain.twitter.TwitterMessage;
import com.jonnypower.tractiondemo.domain.twitter.TwitterMessageVisitor;
import com.jonnypower.tractiondemo.domain.twitter.TwitterStatus;
import com.jonnypower.tractiondemo.service.TwitterService;
import com.jonnypower.tractiondemo.util.TwitterMessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class TwitterMessageQueueConsumer implements Runnable {

    // =========================
    // ATTRIBUTES
    // =========================

    private final TwitterService twitterService;
    private final BlockingQueue<String> messageQueue;
    private final List<String> handleWhitelist;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // =========================
    // CONSTRUCTORS
    // =========================

    public TwitterMessageQueueConsumer(TwitterService twitterService, BlockingQueue<String> messageQueue, List<String> handleWhitelist) {
        this.twitterService = twitterService;
        this.messageQueue = messageQueue;
        this.handleWhitelist = handleWhitelist;
    }

    // =========================
    // IMPLEMENTATION OF Runnable
    // =========================

    @Override
    public void run() {
        try {
            while (true) {

                try {
                    final String message = messageQueue.take();
                    logger.info(message);

                    final JsonNode jsonNode = objectMapper.readTree(message);
                    final Optional<TwitterMessage> twitterMessageOptional = TwitterMessageUtils.getTwitterMessage(objectMapper, jsonNode);

                    if(twitterMessageOptional.isPresent()) {
                        final TwitterMessage twitterMessage = twitterMessageOptional.get();
                        twitterMessage.accept(new TwitterMessageVisitor() {

                            @Override
                            public void visit(TwitterStatus twitterStatus) {
                                logger.info(String.format("%s: %s", twitterStatus.getUser().getScreenName(), twitterStatus.getText()));
                                if(handleWhitelist.contains(twitterStatus.getUser().getScreenName())) {
                                    logger.info("User is in whitelist");
                                    twitterService.replyWithPlug(twitterStatus);
                                }
                            }

                        });
                    }
                } catch (IOException ex) {
                    logger.error("Failed to process message, keeping consumer alive", ex);
                }

            }
        } catch (InterruptedException ex) {
            logger.info("Consumer run interrupted", ex);
        }
    }

}
