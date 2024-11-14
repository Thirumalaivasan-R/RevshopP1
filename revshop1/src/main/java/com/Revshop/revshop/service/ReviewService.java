package com.Revshop.revshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Revshop.revshop.model.Review;
import com.Revshop.revshop.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

	public List<Review> getReviewsByProductId(Long productId) {
		return reviewRepository.findByProductId(productId);
		
	}
}