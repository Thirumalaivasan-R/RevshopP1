package com.Revshop.revshop.controller;

import com.Revshop.revshop.model.Review;
import com.Revshop.revshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
   

    @GetMapping("/writeReview")
    public String showReviewForm(@RequestParam("productId") Long productId, Model model) {
        System.out.println(productId);
        Review review = new Review();  // Create a new Review object
        review.setProductId(productId);  // Set productId in the Review object
        model.addAttribute("review", review);  // Add this review object to the model
        return "review";  // The name of your HTML form
    }


//    // POST mapping to handle form submission
//    @PostMapping("/submitReview")
//    public String submitReview(@ModelAttribute Review review) {
//    	System.out.println(review.getProductId());
//        reviewService.saveReview(review);  // Save the review to the database
//        return "redirect:/product/" + review.getProductId();  // Redirect back to the product page
//    }
    

    @PostMapping("/submitReview")
    public String submitReview(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        System.out.println("Product ID: " + review.getProductId());
        
        reviewService.saveReview(review);  // Save the review to the database
        
        // Add success message to redirect attributes
        redirectAttributes.addFlashAttribute("message", "Review added successfully!");
        
        // Redirect back to the product details page
        return "redirect:/product/details/" + review.getProductId();
    }

}
