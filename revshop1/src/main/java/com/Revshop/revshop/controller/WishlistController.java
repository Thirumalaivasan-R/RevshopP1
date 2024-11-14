package com.Revshop.revshop.controller;

import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.model.WishlistItem;
import com.Revshop.revshop.service.WishlistService;
import com.Revshop.revshop.service.ProductService;
import com.Revshop.revshop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/wishlist/add")
    public String addToWishlist(@RequestParam("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";  // Redirect to login if the user is not logged in
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Product product = productService.getProductById(productId);
            wishlistService.addProductToWishlist(user.get(), product);
        }

        return "redirect:/wishlist";  // Redirect to wishlist page
    }

    @GetMapping("/wishlist")
    public String showWishlist(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";  // Redirect to login if user is not logged in
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            List<WishlistItem> wishlistItems = wishlistService.getWishlistByUser(user.get());
            model.addAttribute("wishlistItems", wishlistItems);
            return "wishlist";  // Refer to wishlist.html template
        }

        return "redirect:/login";
    }

    @PostMapping("/wishlist/remove")
    public String removeFromWishlist(@RequestParam("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";  // Redirect to login if user is not logged in
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            wishlistService.removeProductFromWishlist(user.get(), productId);
        }

        return "redirect:/wishlist";
    }
}