package com.Revshop.revshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Revshop.revshop.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByProductId(Long productId);
}