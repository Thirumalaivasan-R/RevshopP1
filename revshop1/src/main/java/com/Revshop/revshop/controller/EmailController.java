package com.Revshop.revshop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.repository.UserRepository;
import com.Revshop.revshop.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class EmailController {
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/api/register-send-verification")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendRegistrationVerificationCode(@RequestParam("email") String email, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        
        // Ensure that email is not empty or null
        if (email == null || email.isEmpty()) {
            response.put("error", "Email cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if(existingUser.isPresent()) {
        	
        	response.put("error", "User with this email already exists. Please log in.");
        	response.put("redirect", "/login");
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
        	
        	// Generate a random 6-digit OTP
            String verificationCode = String.format("%06d", new java.util.Random().nextInt(999999));

            try {
                // Send the verification email
                emailService.sendVerificationEmail(email, verificationCode);

                // Store the OTP in the session
                session.setAttribute("otp", verificationCode);
                session.setAttribute("email", email);

                response.put("success", "Verification code sent successfully!");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", "Failed to send verification email.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    }
	
	@PostMapping("/api/login-send-verification")
	@ResponseBody
	public ResponseEntity<Map<String, String>> sendLoginVerificationCode(@RequestParam("email") String email, HttpSession session){
		Map<String, String> response = new HashMap<>();
		
		if(email == null || email.isEmpty()) {
			response.put("error", "Email cannot be empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		// Check if the user exists in the database for login
		Optional<User> existingUser = userRepository.findByEmail(email);
		
		if(existingUser.isPresent()) {
			
			 // Generate a random 6-digit OTP
            String verificationCode = String.format("%06d", new java.util.Random().nextInt(999999));
            
            try {
                // Send the verification email
                emailService.sendVerificationEmail(email, verificationCode);

                // Store the OTP and email in the session
                session.setAttribute("otp", verificationCode);
                session.setAttribute("email", email);

                response.put("success", "Verification code sent to email");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put("error", "Failed to send verification email");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            // If the user is not registered, send an error message and suggest registration
            response.put("error", "This email is not registered. Please register first.");
            response.put("redirect", "/registration");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
     }
            
}
		

    @PostMapping("/api/verify-otp")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam("otp") String otp, HttpSession session) {
        String savedOtp = (String) session.getAttribute("otp");

        Map<String, String> response = new HashMap<>();

        if (savedOtp != null && savedOtp.equals(otp)) {
            // OTP verified successfully
            response.put("success", "OTP verified successfully!");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid OTP!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}