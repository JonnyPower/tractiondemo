package com.jonnypower.tractiondemo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    // =========================
    // PUBLIC METHODS
    // =========================

    @RequestMapping("/")
    public String index(@RequestParam(name = "c", required = false) String campaign,
                        @RequestParam(name = "h", required = false) String handle,
                        @RequestParam(name = "s", required = false) String seed,
                        Model model) {
        model.addAttribute("campaign", campaign);
        model.addAttribute("handle", handle);
        model.addAttribute("seed", seed);
        return "index";
    }

}
