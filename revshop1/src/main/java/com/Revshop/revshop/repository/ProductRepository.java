package com.Revshop.revshop.repository;


import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Revshop.revshop.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

 @Query("SELECT p FROM Product p WHERE p.seller.sellerId = :sellerId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.categoryname) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchBySellerAndQuery(Long sellerId, String query);
	 List<Product> findBySeller_SellerId(Long sellerId);
	    @Query("SELECT p FROM Product p WHERE p.seller.sellerId = :sellerId " +
	            "AND (:category IS NULL OR p.categoryname = :category) " +
	            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
	            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
	     List<Product> filterBySellerAndCriteria(@Param("sellerId") Long sellerId, 
	                                              @Param("category") String categoryname, 
	                                              @Param("minPrice") Double price, 
	                                              @Param("maxPrice") Double maxPrice);
	    @Query("SELECT p FROM Product p WHERE " +
	    	       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	    	       "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	    	       "LOWER(p.categoryname) LIKE LOWER(CONCAT('%', :query, '%')))")
	    	List<Product> findByNameOrCategory(@Param("query") String query);


}
