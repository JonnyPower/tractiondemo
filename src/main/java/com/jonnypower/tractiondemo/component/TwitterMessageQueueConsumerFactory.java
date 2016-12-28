package com.jonnypower.tractiondemo.component;

import com.jonnypower.tractiondemo.service.TwitterService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TwitterMessageQueueConsumerFactory implements FactoryBean<TwitterMessageQueueConsumer> {

    // =========================
    // ATTRIBUTES
    // =========================

    @Value("#{'${twitter.stream.whitelist}'.split(',')}")
    private List<String> whitelist;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    @Qualifier("twitterMessageQueue")
    private BlockingQueue<String> messageQueue;

    // =========================
    // IMPLEMENTATION OF FactoryBean
    // =========================

    @Override
    public TwitterMessageQueueConsumer getObject() throws Exception {
        return new TwitterMessageQueueConsumer(twitterService, messageQueue, whitelist);
    }

    @Override
    public Class<?> getObjectType() {
        return TwitterMessageQueueConsumer.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
