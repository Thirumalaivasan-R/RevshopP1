package com.Revshop.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.Review;
import com.Revshop.revshop.service.ProductService;
import com.Revshop.revshop.service.ReviewService;

@Controller
public class HomeController {
	@Autowired
	private UserController user;
	@Autowired
    private ProductService productService;
	@Autowired
	private ReviewService reviewService;
	@GetMapping("/home")
    public String showHomePage(Model model) {
        // Add data to the model for dynamic content (like featured products)
        model.addAttribute("featuredProducts", productService.getAllProducts());
        
        return "home-page";  
    }
	
	
	

	    @GetMapping("product/details/{id}")
	    public String productDetails(@PathVariable Long id, Model model) {
	        // Assuming you have a ProductService to fetch product by ID
	        Product product = productService.getProductById(id);
	        List<Review> reviews = reviewService.getReviewsByProductId(id);
	        model.addAttribute("product", product);
	        model.addAttribute("reviews", reviews);
	        return "product"; // This will render product.html
	   
	}


}
