package com.Revshop.revshop.controller;

import com.Revshop.revshop.model.CartItem;
import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.service.CartService;
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
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Product product = productService.getProductById(productId);
            cartService.addProductToCart(user.get(), product, quantity);
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            List<CartItem> cartItems = cartService.getCartItemsByUser(user.get());
            List<CartItem> savedItems = cartService.getSavedItemsByUser(user.get());
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("savedItems", savedItems);

            // Calculate price details
            model.addAttribute("totalPrice", cartService.calculateTotalPrice(cartItems));
            model.addAttribute("discount", cartService.calculateDiscount(cartItems));
            model.addAttribute("deliveryCharges", cartService.calculateDeliveryCharges(cartItems));
            model.addAttribute("totalAmount", cartService.calculateFinalAmount(cartItems));
            return "cart";
        }

        return "redirect:/login";
    }

    @PostMapping("/cart/update-quantity")
    public String updateCartItemQuantity(@RequestParam("productId") Long productId,
                                         @RequestParam("increase") boolean increase, HttpSession session) {
        String email = (String) session.getAttribute("email");
        
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            cartService.updateCartItemQuantity(user.get(), productId, increase);
        }
        
        return "redirect:/cart";
    }


    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            cartService.removeCartItem(user.get(), productId);
        }
        
        return "redirect:/cart";
    }


    @PostMapping("/cart/save-for-later")
    public String saveForLater(@RequestParam("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            cartService.saveForLater(user.get(), productId);
        }
        
        return "redirect:/cart";
    }


    // Move item back to cart from saved items
    @GetMapping("/cart/move-to-cart")
    public String moveToCart(@RequestParam("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                cartService.moveToCart(user.get(), productId);
            }
        }
        return "redirect:/cart";
    }
}
