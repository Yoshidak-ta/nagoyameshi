package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
//	登録用
	@Transactional
	public void create(Store store, User user, ReviewForm reviewForm) {
		Review review = new Review();		
		
		review.setStore(store);
		review.setUser(user);
		review.setScore(reviewForm.getScore());
		review.setContent(reviewForm.getContent());
		
		reviewRepository.save(review);
	}
	
//	更新用
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());
		
		reviewRepository.save(review);
	}
	
	public boolean reviewJudge(Store store, User user) {
		Review review = reviewRepository.findByUserAndStore(user, store);
		return review != null;
	}

}
