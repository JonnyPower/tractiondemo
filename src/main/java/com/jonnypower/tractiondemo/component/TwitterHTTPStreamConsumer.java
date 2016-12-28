package com.jonnypower.tractiondemo.component;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.*;

@Component
public class TwitterHTTPStreamConsumer {

    // =========================
    // ATTRIBUTES
    // =========================

    @Value("${twitter.stream.app.key}")
    private String appKey;

    @Value("${twitter.stream.app.secret}")
    private String appSecret;

    @Value("${twitter.stream.access.token}")
    private String accessToken;

    @Value("${twitter.stream.access.secret}")
    private String accessSecret;

    @Value("#{'${twitter.stream.terms}'.split(',')}")
    private List<String> terms;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("twitterMessageQueue")
    private BlockingQueue<String> messageQueue;

    @Autowired
    @Qualifier("twitterMessageQueueConsumerExecutorService")
    private ThreadPoolExecutor executorService;

    @Autowired
    private TwitterMessageQueueConsumerFactory consumerFactory;

    private Client client;

    // =========================
    // SETUP
    // =========================

    @PostConstruct
    public void connect() {

        final Hosts hosts = new HttpHosts(Constants.STREAM_HOST);
        final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(terms);
        final Authentication authentication = new OAuth1(appKey, appSecret, accessToken, accessSecret);

        final ClientBuilder builder = new ClientBuilder()
                .name(this.getClass().getCanonicalName())
                .hosts(hosts)
                .authentication(authentication)
                .endpoint(endpoint)
                .processor(new StringDelimitedProcessor(messageQueue));

        this.client = builder.build();

        logger.info("Client connecting...");
        client.connect();
        logger.info("Client connected.");

        logger.info("Setting up consumer threads...");
        for(int i = 0; i < executorService.getMaximumPoolSize(); i++) {
            try {
                executorService.execute(consumerFactory.getObject());
            } catch (Exception ex) {
                logger.error("Failed to execute consumer form consumerFactory", ex);
            }
        }
        logger.info("Set up consumer threads.");
    }

    @PreDestroy
    public void disconnect() {
        client.stop();
    }

}
