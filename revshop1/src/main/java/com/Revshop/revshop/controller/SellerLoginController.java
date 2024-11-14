package com.Revshop.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Revshop.revshop.model.Seller;
import com.Revshop.revshop.service.SellerLoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SellerLoginController {
	
	@Autowired
	private SellerLoginService sellerLoginService;
	
	@GetMapping("/sellerLogin")
	public String showLoginForm(Model model) {
	    model.addAttribute("seller", new Seller());
	    return "seller-login";
	}

	@PostMapping("/sellerLogin")
	public String loginSeller(@ModelAttribute("seller") Seller seller, Model model,HttpSession session) {
	    if(sellerLoginService.isSellerRegistered(seller.getEmail(), seller.getPassword())) {
	    	Seller loggedInSeller=sellerLoginService.findByemail(seller.getEmail());
	    	model.addAttribute("message", "Login successful!");
          session.setAttribute("loggedInSeller", loggedInSeller);
          
          
	        return "seller-dashboard";  // Redirect after successful login
	    } else {
	        model.addAttribute("errorMessage", "Invalid Email or Password");
	        return "seller-login";
	    }
	}
//	public String loginSeller(@ModelAttribute("seller") Seller seller, Model model,HttpSession session) {
//        boolean isValidSeller = service.validateSeller(seller.getEmail(), seller.getPassword());
//        
//        if (isValidSeller) {
//        	Seller loggedInSeller=service.findbyemail(seller.getEmail());
//            model.addAttribute("message", "Login successful!");
//            session.setAttribute("loggedInSeller", loggedInSeller); 
//            return "sellerDahboard";  
//        } else {
//            model.addAttribute("loginError", "Invalid email or password");
//            return "loginSeller";  
//        }
//    }

}
