package com.Revshop.revshop.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.Seller;
import com.Revshop.revshop.repository.ProductRepository;
import com.Revshop.revshop.repository.SellerRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
	@Autowired
	private ProductRepository repo;
	@Autowired
	private SellerRepository srepo;
//	public void addProduct(Product product)
//	{
//		System.out.println(product.getName());
//		System.out.println(product.getSeller());
//		repo.save(product);
//	}
	public void addProduct(Product product) {                  
	    Seller seller = product.getSeller();
	    
	    if (seller != null && seller.getSellerId() != null) {
	        // Re-fetch seller to make sure it's attached to the session
	        Seller managedSeller =srepo .findById(seller.getSellerId())
	                                 .orElseThrow(() -> new RuntimeException("Seller not found"));
	        product.setSeller(managedSeller);
	    }
	    product.setProductAddedAt(LocalDate.now());
	    
	   repo.save(product);  
	}
	
	public List<Product> getAllProducts() {
		
	        return repo.findAll();  
	    
	}
	

//	public List<Product> searchProducts(String query,Long sellerId) {
//		return repo.findBySeller_SelleridAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategorynameContainingIgnoreCase(
//                sellerId, query, query, query);
//    }
//	public List<Product> searchProducts(String query, Long sellerId) {
//        // Log the inputs for debugging
//		if (query == null || query.trim().isEmpty()) {
//            return List.of(); // Return an empty list if no query
//        }
//
//        return repo.findBySeller_SelleridAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategorynameContainingIgnoreCase(
//                sellerId, query, query, query);
//    }
	public List<Product> searchProducts(String query, Long sellerId) {
        // Check if the query is null or empty to avoid fetching all products
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Return an empty list if no query
        }

        return repo.searchBySellerAndQuery(sellerId, query);
    }

    
	
	public Product getProductById(Long productId) {
	    return repo.findById(productId).orElse(null);  
	}

	public void updateProduct(Product product) {
     Seller seller = product.getSeller();
	    
	    if (seller != null && seller.getSellerId() != null) {
	       
	        Seller managedSeller =srepo .findById(seller.getSellerId())
	                                 .orElseThrow(() -> new RuntimeException("Seller not found"));
	        product.setSeller(managedSeller);
	    }
	    product.setProductAddedAt(LocalDate.now());
	    
	   repo.save(product);  
		
	    repo.save(product);  
	}
	public void deleteProduct(Long productId) {
		repo.deleteById(productId);
		// TODO Auto-generated method stub
		
	}

	


	


	public List<Product> getProductsBySellerId(Long sellerId) {
        return repo.findBySeller_SellerId(sellerId);  // Fetch the products by sellerId
    }
	public List<Product> searchProductsByNameOrCategory(String query) {
		if (query == null || query.trim().isEmpty()) {
            return List.of(); // Return an empty list if no query
        }
        return repo.findByNameOrCategory(query);
    }
	

    

	 
}
