package com.neighbour_snack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "NS - Admin Control Panel");
        return "admin/dashboard";
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("title", "NS - Category");
        return "admin/category";
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("title", "NS - Product");
        return "admin/product";
    }

}
