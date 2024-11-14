package com.Revshop.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.Revshop.revshop.repository.UserRepository;
import com.Revshop.revshop.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import com.Revshop.revshop.model.User;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.*;


@Controller 
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private UserService userService;

    // Get mapping to show the registration page
    @GetMapping("/registration")
    public String showRegistrationForm() {
    
        return "registration";  
    }

    @PostMapping("/api/register-user")
    @ResponseBody
    public Map<String, String> registerUser(@RequestBody User user, HttpSession session) {
        
    	Map<String, String> response = new HashMap<>();
    	// Retrieve the verified email from session
        String verifiedEmail = (String) session.getAttribute("email");

        if (verifiedEmail != null) {
            // Set the verified email in the user object
            user.setEmail(verifiedEmail);
            user.setEmailVerified(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // Save the user to the repository (database)
           User savedUser = userRepository.save(user);
           
           session.setAttribute("userId", savedUser.getUserId());

            // Clear session after registration
            session.removeAttribute("otp");
            session.removeAttribute("email");

            response.put("status", "success");
            response.put("redirect", "/profile");
            return response;
        } else {
            response.put("status", "error");
            response.put("message", "Email verification required!");
            return response;
        }
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  
    }
   
    @PostMapping("/api/login-user")
    @ResponseBody
    public Map<String, String> loginUser(@RequestBody Map<String, String> requestBody, HttpSession session, Model model) {
    	Map<String, String> response = new HashMap<>();
        String savedOtp = (String) session.getAttribute("otp");
        String verifiedEmail = (String) session.getAttribute("email");
        String enteredOtp = requestBody.get("otp");

        if (verifiedEmail != null && savedOtp != null) {
            if (savedOtp.equals(enteredOtp)) {
                Optional<User> existingUser = userRepository.findByEmail(verifiedEmail);

                if (existingUser.isPresent()) {
                    User user = existingUser.get();
                    user.setLastLoginAt(LocalDateTime.now());
                    userRepository.save(user);

                    // Set the email in the session for further use
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("email", verifiedEmail);
                    
 
                    response.put("status", "success");
                    response.put("redirect", "/home");
                    return response;
                } else {
                	response.put("status", "error");
                    response.put("message", "Please register to login.");
                    return response;
                }
            } else {
            	 response.put("status", "error");
                 response.put("message", "Invalid OTP!");
                 return response;
            }
        } else {
        	 response.put("status", "error");
             response.put("message", "Email verification required!");
             return response;
        }
    }

    
    
    @GetMapping("/profile")
    public String getUserProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                
                List<String> states = Arrays.asList(
                        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", 
                        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", 
                        "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", 
                        "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", 
                        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
                        "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli", 
                        "Daman and Diu", "Lakshadweep", "Delhi", "Puducherry"
                    );
                    model.addAttribute("states", states);
                return "user-profile";
            }
        }
        return "redirect:/login";  // Redirect to login if email is not found
    }


    @PostMapping("/api/update-profile")
    @ResponseBody
    public Map<String, String> updateProfile(@RequestBody Map<String, String> updatedFields, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> existingUser = userService.getUserById(userId);
            if (existingUser.isPresent()) {
                User user = existingUser.get();

                // Update the fields only if they are present in the request
                if (updatedFields.containsKey("firstName")) {
                    user.setFirstName(updatedFields.get("firstName"));
                }
                if (updatedFields.containsKey("lastName")) {
                    user.setLastName(updatedFields.get("lastName"));
                }
                if (updatedFields.containsKey("email")) {
                    user.setEmail(updatedFields.get("email"));
                }
                if (updatedFields.containsKey("phoneNumber")) {
                    user.setPhoneNumber(updatedFields.get("phoneNumber"));
                }
                if (updatedFields.containsKey("gender")) {
                    user.setGender(updatedFields.get("gender"));
                }
                
                // Save the updated user information
                userService.updateUserDetails(user);

                response.put("status", "success");
                return response;
            }
        }
        response.put("status", "error");
        response.put("message", "User not found or session expired.");
        return response;
    }

    @PostMapping("/profile/update-address")
    @ResponseBody
    public Map<String, String> updateAddress(@RequestBody User updatedAddress, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            Optional<User> existingUser = userService.getUserById(userId);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setFullName(updatedAddress.getFullName());
                user.setPhoneNumber(updatedAddress.getPhoneNumber());
                user.setAddress(updatedAddress.getAddress());
                user.setLocality(updatedAddress.getLocality());
                user.setPincode(updatedAddress.getPincode());
                user.setCity(updatedAddress.getCity());
                user.setState(updatedAddress.getState());
                user.setLandmark(updatedAddress.getLandmark());
                user.setAddressType(updatedAddress.getAddressType());

                // Save the updated address information
                userService.updateUserDetails(user);

                response.put("status", "success");
                return response;
            }
        }
        response.put("status", "error");
        response.put("message", "User not found or session expired.");
        return response;
    }
    
    
    @DeleteMapping("/api/delete-account")
    @ResponseBody
    public Map<String, String> deleteAccount(HttpSession session) {
        Map<String, String> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            // Delete user from the database
            userService.deleteUserById(userId);

            // Invalidate session
            session.invalidate();

            response.put("status", "success");
        } else {
            response.put("status", "error");
            response.put("message", "User not found or session expired.");
        }
        return response;
    }


    
    
//other features of myprofile yet to be impelemented 
    /*
@PostMapping("/wishlist/add")
public String addToWishlist(@RequestParam("productId") Long productId, HttpSession session) {
    String email = (String) session.getAttribute("email");

    if (email != null) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Product product = productRepository.findById(productId).orElse(null); // Assuming productRepository exists
            if (product != null) {
                user.get().getWishlist().add(product);
                userRepository.save(user.get());
                return "redirect:/wishlist";
            }
        }
    }
    return "redirect:/login";
}

@PostMapping("/wishlist/remove")
public String removeFromWishlist(@RequestParam("productId") Long productId, HttpSession session) {
    String email = (String) session.getAttribute("email");

    if (email != null) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Product product = productRepository.findById(productId).orElse(null); // Assuming productRepository exists
            if (product != null) {
                user.get().getWishlist().remove(product);
                userRepository.save(user.get());
                return "redirect:/wishlist";
            }
        }
    }
    return "redirect:/login";
}

@GetMapping("/orders")
public String getOrderHistory(HttpSession session, Model model) {
    String email = (String) session.getAttribute("email");

    if (email != null) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            List<Order> orders = orderRepository.findByUser(user.get()); // Assuming orderRepository exists
            model.addAttribute("orders", orders);
            return "order-history";  // Assuming there's a Thymeleaf template for order history
        }
    }
    return "redirect:/login";
}


     */
}