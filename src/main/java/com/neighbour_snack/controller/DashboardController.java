package com.neighbour_snack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "Neighbour Snack");
        return "dashboard/dashboard";
    }

}
