package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.MenuRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
public class HomeController {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final ReviewRepository reviewRepository;

	public HomeController(StoreRepository storeRepository, MenuRepository menuRepository,
			CategoryRepository categoryRepository, ReviewRepository reviewRepository) {
		this.storeRepository = storeRepository;
		this.categoryRepository = categoryRepository;
		this.reviewRepository = reviewRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Store> newStores = storeRepository.findTop10ByOrderByCreatedAtDesc();
		List<Category> categoryList = categoryRepository.findAll();

		for (Store store : newStores) {
			List<Review> reviewList = reviewRepository.findByStore(store);
			Double averageScore = reviewRepository.findAverageScoreByStore(store);
			if (averageScore != null) {
				store.setAverageScore(averageScore);
			}
			if (reviewList != null) {
				store.setReview(reviewList);
			}
		}

		model.addAttribute("newStores", newStores);
		model.addAttribute("categoryList", categoryList);

		return "index";
	}
}
