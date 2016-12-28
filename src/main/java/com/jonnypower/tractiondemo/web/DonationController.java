package com.jonnypower.tractiondemo.web;

import com.google.common.base.Strings;
import com.jonnypower.tractiondemo.exception.SalesforceOAuthSessionNotSetupException;
import com.jonnypower.tractiondemo.service.SalesforceService;
import com.jonnypower.tractiondemo.service.StripeService;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CurrencyEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.Currency;
import java.util.Optional;

@Controller
public class DonationController {

    // =========================
    // ATTRIBUTES
    // =========================

    @Autowired
    private StripeService stripeService;

    @Autowired
    private SalesforceService salesforceService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // =========================
    // SETUP
    // =========================

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Currency.class, new CurrencyEditor() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                super.setAsText(text.toUpperCase());
            }
        });
    }

    // =========================
    // PUBLIC METHODS
    // =========================

    @RequestMapping(value = "/donate", method = RequestMethod.POST)
    public String donate(@RequestParam String stripeEmail,
                         @RequestParam String stripeToken,
                         @RequestParam int amount,
                         @RequestParam Currency currency,
                         @RequestParam(required = false) String campaign,
                         @RequestParam(required = false) String handle,
                         @RequestParam(required = false) String seed,
                         Model model) {
        final Optional<Charge> chargeOptional = stripeService.charge(stripeToken, amount, currency);
        model.addAttribute("success", chargeOptional.isPresent());
        if(chargeOptional.isPresent()) {
            model.addAttribute("charge", chargeOptional.get());
            final boolean donationAttributed = !(Strings.isNullOrEmpty(campaign) || Strings.isNullOrEmpty(handle) || Strings.isNullOrEmpty(seed));
            if(donationAttributed) {
                final String donationId = salesforceService.registerDonation(campaign, handle, seed, amount);
                model.addAttribute("donationId", donationId);
            }
        }
        return "donate";
    }

}
