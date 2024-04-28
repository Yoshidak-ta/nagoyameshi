package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Menu;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.MenuRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
@RequestMapping("/stores/{id}")
public class MenuController {
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;

	public MenuController(MenuRepository menuRepository, StoreRepository storeRepository,
			ReviewRepository reviewRepository) {
		this.menuRepository = menuRepository;
		this.storeRepository = storeRepository;
		this.reviewRepository = reviewRepository;
	}

	@GetMapping("/menus")
	public String index(@PathVariable(name = "id") Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		Menu menu = menuRepository.getReferenceById(id);
		int price = (int) (menu.getPrice() * 1.1);

		List<Review> reviewList = reviewRepository.findByStore(store);
		Double averageScore = reviewRepository.findAverageScoreByStore(store);
		if (averageScore == null) {
			averageScore = 0.0;
		}

		model.addAttribute("averageScore", averageScore);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("store", store);
		model.addAttribute("menu", menu);
		model.addAttribute("price", price);

		return "menus/index";
	}

}
