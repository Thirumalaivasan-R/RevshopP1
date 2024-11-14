package com.Revshop.revshop.service;

import com.Revshop.revshop.model.CartItem;
import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.User;
import com.Revshop.revshop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public void addProductToCart(User user, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findByUserUserId(user.getUserId())
                .stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setSavedForLater(false);  // Ensure it's in the cart, not saved for later
        cartItemRepository.save(cartItem);
    }

    public void updateCartItemQuantity(User user, Long productId, boolean increase) {
        List<CartItem> cartItems = cartItemRepository.findByUserUserId(user.getUserId());
        
        CartItem cartItem = cartItems.stream()
                                     .filter(item -> item.getProduct().getProductId().equals(productId))
                                     .findFirst()
                                     .orElse(null);
        
        if (cartItem != null) {
            int newQuantity = increase ? cartItem.getQuantity() + 1 : cartItem.getQuantity() - 1;
            if (newQuantity <= 0) {
                cartItemRepository.delete(cartItem);  // Remove from cart if quantity is 0
            } else {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
        }
    }


    public void removeCartItem(User user, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserUserId(user.getUserId())
                                              .stream()
                                              .filter(item -> item.getProduct().getProductId().equals(productId))
                                              .findFirst()
                                              .orElse(null);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }
    }


    public void saveForLater(User user, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserUserId(user.getUserId())
                                              .stream()
                                              .filter(item -> item.getProduct().getProductId().equals(productId))
                                              .findFirst()
                                              .orElse(null);

        if (cartItem != null) {
            cartItem.setSavedForLater(true);  // Assuming 'savedForLater' is a boolean field in CartItem
            cartItemRepository.save(cartItem);
        }
    }


    public void moveToCart(User user, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserUserId(user.getUserId())
                .stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setSavedForLater(false);
            cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUserUserId(user.getUserId())
                .stream()
                .filter(item -> !item.isSavedForLater())  // Only return items in the cart
                .toList();
    }

    public List<CartItem> getSavedItemsByUser(User user) {
        return cartItemRepository.findByUserUserId(user.getUserId())
                .stream()
                .filter(CartItem::isSavedForLater)  // Only return saved-for-later items
                .toList();
    }

    // Price Calculation Methods
    public double calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public double calculateDiscount(List<CartItem> cartItems) {
        // Example: flat 10% discount
        return calculateTotalPrice(cartItems) * 0.1;
    }

    public double calculateDeliveryCharges(List<CartItem> cartItems) {
        // Example: flat delivery charge
    	return cartItems.isEmpty() ? 0 : 50;
    }

    public double calculateFinalAmount(List<CartItem> cartItems) {
        return calculateTotalPrice(cartItems) - calculateDiscount(cartItems) + calculateDeliveryCharges(cartItems);
    }

	public Object getSavedForLaterItems(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
