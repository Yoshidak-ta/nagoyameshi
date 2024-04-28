package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Seat;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.SeatRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
@RequestMapping("/stores/{id}")
public class SeatController {
	private final SeatRepository seatRepository;
	private final StoreRepository storeRepository;
	private final ReviewRepository reviewRepository;

	public SeatController(SeatRepository seatRepository, StoreRepository storeRepository,
			ReviewRepository reviewRepository) {
		this.seatRepository = seatRepository;
		this.storeRepository = storeRepository;
		this.reviewRepository = reviewRepository;

	}

	@GetMapping("/seats")
	public String index(@PathVariable(name = "id") Integer id, Model model) {
		Seat seat = seatRepository.getReferenceById(id);
		Store store = storeRepository.getReferenceById(id);

		List<Review> reviewList = reviewRepository.findByStore(store);
		Double averageScore = reviewRepository.findAverageScoreByStore(store);
		if (averageScore == null) {
			averageScore = 0.0;
		}

		model.addAttribute("averageScore", averageScore);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("seat", seat);
		model.addAttribute("store", store);

		return "seats/index";

	}

}
