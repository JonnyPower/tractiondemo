package com.jonnypower.tractiondemo.web;

import com.jonnypower.tractiondemo.service.SalesforceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SalesforceController {

    @Autowired
    private SalesforceService salesforceService;

    @Value("${salesforce.request.secret:supersecretauthflow}")
    private String requestSecret;

    @RequestMapping("/salesforce/request")
    public String requestUrl(@RequestParam String secret, Model model) {
        if (requestSecret.equals(secret)) {
            model.addAttribute("requestUrl", salesforceService.getOAuthRequestUrl());
        }
        return "request";
    }

    @RequestMapping("/salesforce/callback")
    public String callback(@RequestParam String code, Model model) {
        final boolean success = salesforceService.completeOAuth(code);
        model.addAttribute("success", success);
        return "callback";
    }

}
