package com.jonnypower.tractiondemo;

import com.jonnypower.tractiondemo.component.TwitterMessageQueueConsumerFactory;
import com.jonnypower.tractiondemo.exception.SalesforceOAuthSessionNotSetupException;
import com.twitter.hbc.core.event.Event;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;
import java.util.concurrent.*;

@Configuration
@SpringBootApplication
public class TractionDemoApplication {

    // =========================
    // APPLICATION CONTEXT
    // =========================

    @Bean
    public ThreadPoolExecutor twitterMessageQueueConsumerExecutorService() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    @Bean
    public TwitterMessageQueueConsumerFactory twitterMessageQueueConsumerFactory() {
        return new TwitterMessageQueueConsumerFactory();
    }

    @Bean
    public BlockingQueue<String> twitterMessageQueue() {
        return new LinkedBlockingQueue(1000);
    }

	@Bean
    public ConversionService conversionService() {
		return new DefaultConversionService();
	}

    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r =
                new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        //mappings.setProperty(SalesforceOAuthSessionNotSetupException.class.getSimpleName(), "OAuthError");

        r.setExceptionMappings(mappings);
        r.setDefaultErrorView("error");
        return r;
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {
		SpringApplication.run(TractionDemoApplication.class, args);
	}
}
