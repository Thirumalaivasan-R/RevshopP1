package com.Revshop.revshop.controller;

import java.time.LocalDate;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Revshop.revshop.dto.OrderDTO;
import com.Revshop.revshop.model.CartItem;
import com.Revshop.revshop.model.OrderDetails;
import com.Revshop.revshop.model.Orders;
import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.Seller;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.repository.CartItemRepository;
import com.Revshop.revshop.repository.OrderDetailsRepository;
import com.Revshop.revshop.repository.OrdersRepository;
import com.Revshop.revshop.repository.ProductRepository;
import com.Revshop.revshop.repository.UserRepository;
import com.Revshop.revshop.service.BuyerOrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BuyerOrderController {

	@Autowired
    private ProductRepository productRepo;
	@Autowired
    private OrdersRepository ordersRepo;    
    @Autowired
    private OrderDetailsRepository orderDetailsRepo;
	@Autowired
	private BuyerOrderService buyerOrderService;
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/checkout")
	public String handleCheckout(@RequestParam("totalPrice") double totalPrice,
		
			HttpSession session, Model model) {
	    
	    String email = (String)session.getAttribute("email");
	    if (email == null) {
            return "redirect:/login";
        }
	    System.out.println(totalPrice);
	    //System.out.println(userId);
	    model.addAttribute("totalPrice", totalPrice);
       // model.addAttribute("userId", userId);

        return "order-address";
//	    Optional<User> user = userRepo.findByEmail(email);
//        if (user.isPresent()) {
//        	 buyerOrderService.createOrders(email, totalPrice);
//        }
//        else
//        {
//        	return "redirect:/login";
//        } 	    
//	    return "order-address"; 
	}
	
	@PostMapping("/submitAddress")
	public String submitAddress(@RequestParam("name") String name, 
			@RequestParam("phone") String phone,
			@RequestParam("address") String address,
			@RequestParam("state") String state,
			@RequestParam("zipcode") String zipcode, 
			@RequestParam("totalPrice") double totalPrice,
			HttpSession session, Model model)
	{
		String email = (String)session.getAttribute("email");
	    if (email == null) {
            return "redirect:/login";
        }
	    Optional<User> user = userRepo.findByEmail(email);
      if (user.isPresent()) {
      	 buyerOrderService.createOrders(email, totalPrice, name,
      			 phone, address, state, zipcode);
      }
      else
      {
      	return "redirect:/login";
      } 	
		return "orderPlaced";
	}
	@GetMapping("/viewOrders")
    public String viewOrders(HttpSession session, Model model) 
	{
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getUserId();

            // Step 1: Fetch all orders for the user
            List<Orders> orders = ordersRepo.findOrdersByUser_UserId(userId);
            List<OrderDTO> buyerOrders = new ArrayList<>();

            // Step 2: Loop through each order
            for (Orders order : orders) {
                // Step 3: Get the order details for each order
                var orderDetails = orderDetailsRepo.findByOrder_OrderId(order.getOrderId());
                LocalDate orderDate = order.getOrderDate();
                // Step 4: For each order detail, get product details
                for (var orderDetail : orderDetails) {
                    var productOpt = productRepo.findById(orderDetail.getProduct().getProductId());
                    if (productOpt.isPresent()) {
                        var product = productOpt.get();
                        OrderDTO orderDTO = new OrderDTO(
                        		product.getProductId(),
                                product.getName(),
                                product.getImage(),
                                product.getDiscountprice(),
                                orderDetail.getQuantity(),
                                orderDate
                        );
                        buyerOrders.add(orderDTO);
                    }
                }
            }

            // Step 5: Add the order details to the model
            model.addAttribute("buyerOrders", buyerOrders);
            return "BuyerOrders"; // The view to display order details
        } else {
            return "redirect:/login";
        }
    }
	@GetMapping("/viewSellerOrders")
    public String viewSellerOrders(HttpSession session, Model model) 
	{
        Seller loggedInSeller = (Seller) session.getAttribute("loggedInSeller");
        Long sellerId = loggedInSeller.getSellerId();
        if (sellerId != null) {
            // Fetch orders for the seller
        	List<Orders> orders = ordersRepo.findAll(); // Fetch all orders
        	List<OrderDTO> sellerOrders = new ArrayList<>();

        	// Step 1: Loop through all the orders
        	for (Orders order : orders) {
        	    LocalDate orderDate = order.getOrderDate();

        	    // Step 2: Fetch order details for the current order from orderDetailsRepo
        	    List<OrderDetails> orderDetails = orderDetailsRepo.findByOrder_OrderId(order.getOrderId());

        	    // Step 3: Loop through each OrderDetails and filter by Seller ID
        	    for (OrderDetails orderDetail : orderDetails) {
        	        Product product = orderDetail.getProduct();

        	        // Step 4: Check if the product belongs to the logged-in seller
        	        if (product.getSeller().getSellerId().equals(sellerId)) {

        	            // Step 5: Create the SellerOrderDTO with the required details
        	            OrderDTO orderDTO = new OrderDTO(
        	                    product.getProductId(),
        	                    product.getName(),
        	                    product.getImage(),
        	                    product.getDiscountprice(),
        	                    orderDetail.getQuantity(),
        	                    orderDate // Order date from Orders table
        	            );

        	            // Step 6: Add the DTO to the list
        	            sellerOrders.add(orderDTO);
        	        }
        	    }
        	}

        	// Now sellerOrderDetailsList contains all the details of products ordered from the logged-in seller.

            model.addAttribute("sellerOrders", sellerOrders);
            return "sellerOrders";
        } else {
            return "seller-login";
        }
		
    }

	
}