package com.Revshop.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Revshop.revshop.model.Seller;
import com.Revshop.revshop.service.SellerRegistrationService;

@Controller
public class SellerRegistrationController {
	
	@Autowired
	private SellerRegistrationService sellerRegistrationService;
	
	@GetMapping("/sellerRegistration")
	public String showRegistrationForm(Model model)
	{
		model.addAttribute("seller", new Seller());
		return "seller-registration"; //html SllerRegistration 
	}
	
	@PostMapping("/sellerRegistration")
	public String registerSeller(@ModelAttribute("seller") Seller seller, Model model)
	{
		if(sellerRegistrationService.isSellerDataValid(seller))
		{
			sellerRegistrationService.registerSeller(seller);
			return "redirect:/sellerLogin"; //returns to sellerLogin html page
		}
		else
		{
			model.addAttribute("errorMessage", "All fields are required");
			return "seller-registration"; //return again to html registration page
		}
		
	}
}

