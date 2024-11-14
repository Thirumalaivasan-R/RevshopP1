package com.Revshop.revshop.service;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Revshop.revshop.model.CartItem;
import com.Revshop.revshop.model.OrderDetails;
import com.Revshop.revshop.model.Orders;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.repository.CartItemRepository;
import com.Revshop.revshop.repository.OrderDetailsRepository;
import com.Revshop.revshop.repository.OrdersRepository;
import com.Revshop.revshop.repository.UserRepository;

@Service
public class BuyerOrderService {

	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CartItemRepository cartRepo;
	@Autowired
	private OrdersRepository orderRepo;
	@Autowired
	private OrderDetailsRepository orderDetailsRepo;
	
	public void createOrders(String email, double totalPrice,
			String name, String phone, String address, String state,
			String zipcode)
	{
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
	    long userId = user.getUserId();	    
	    List<CartItem> cartList = cartRepo.findByUserUserId(userId);
	    Orders orders = new Orders();
	    orders.setUser(user);
	    orders.setOrderDate(LocalDate.now());
	    orders.setShippedDate(LocalDate.now().plusDays(2));
	    orders.setRequiredDate(LocalDate.now().plusDays(5));
	    orders.setName(name);
	    orders.setPhone(phone);
	    orders.setAddress(address);
	    orders.setState(state);
	    orders.setZipcode(zipcode);
	    orderRepo.save(orders);
	    for(CartItem c : cartList)
	    {
	    	OrderDetails orderDetails = new OrderDetails();
	    	orderDetails.setOrder(orders);
	    	orderDetails.setProduct(c.getProduct());
	    	orderDetails.setQuantity(c.getQuantity());
	    	orderDetailsRepo.save(orderDetails);
	    }
	    sendOrderConfirmationEmail(user.getEmail(), orders, totalPrice);
	    cartRepo.deleteAll(cartList);
	}
	
	private void sendOrderConfirmationEmail(String toEmail, Orders orders, double totalPrice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Order Confirmation - RevShop");
        message.setText("Dear Customer, \n\nYour order with order ID " + orders.getOrderId() +"\n total price: "+totalPrice+ " has been confirmed. "
                + "Thank you for shopping with us!\n\nBest Regards,\nRevShop Team");
        
        // Send email
        mailSender.send(message);
    }
}