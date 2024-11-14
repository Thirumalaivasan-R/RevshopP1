package com.Revshop.revshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerDashboardController {
	@GetMapping("/seller-dashboard")
	public String sellerDashboard(Model model) {
	    // Add attributes to the model if needed
	    return "seller-dashboard"; // Make sure this matches the name of your HTML file (without .html)
	}

}
