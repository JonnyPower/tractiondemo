package com.jonnypower.tractiondemo.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeService {

    // =========================
    // ATTRIBUTES
    // =========================

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // =========================
    // SETUP
    // =========================

    @PostConstruct
    public void setup() {
        Stripe.apiKey = stripeApiKey;
    }

    // =========================
    // PUBLIC METHODS
    // =========================

    public Optional<Charge> charge(String stripeToken, int amount, Currency currency) {
        try {
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", amount); // Amount in cents
            chargeParams.put("currency", currency.getCurrencyCode());
            chargeParams.put("source", stripeToken);
            chargeParams.put("description", "Donation");

            Charge charge = Charge.create(chargeParams);
            return Optional.of(charge);
        } catch (CardException ex) {
            logger.error("Card declined", ex);
        } catch (APIException ex) {
            logger.error("API exception when charging", ex);
        } catch (InvalidRequestException ex) {
            logger.error("Invalid request when charging", ex);
        } catch (APIConnectionException ex) {
            logger.error("API Connection exception when charging", ex);
        } catch (AuthenticationException ex) {
            logger.error("Authentication exception when charging", ex);
        }

        return Optional.empty();
    }

}
