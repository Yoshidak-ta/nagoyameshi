package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationConfirmForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.MenuRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	private final StoreRepository storeRepository;
	private final StripeService stripeService;
	private final CategoryRepository categoryRepository;
	
	public HomeController(StoreRepository storeRepository, MenuRepository menuRepository, StripeService stripeService, CategoryRepository categoryRepository) {
		this.storeRepository = storeRepository;
		this.stripeService = stripeService;
		this.categoryRepository = categoryRepository;
	}
	
    @GetMapping("/")
    public String index(Model model) {
    	List<Store> newStores = storeRepository.findTop10ByOrderByCreatedAtDesc();
    	List<Category> categoryList = categoryRepository.findAll();
    	
    	model.addAttribute("newStores", newStores);
    	model.addAttribute("categoryList", categoryList);
    	
    	return "index";
  }
    
    @GetMapping("/primeregister")
    public String primeregister(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		                   @ModelAttribute ReservationConfirmForm reservationConfirmForm,
    		                   HttpServletRequest httpServletRequest, 
    		                   Model model) {
    	
    	User user = userDetailsImpl.getUser();
    	int userRole = user.getRole().getId();
    	
    	if(userRole == 1) {
	          model.addAttribute("user", user);
	    	  model.addAttribute("errorMessage", "下記機能をご利用の場合は有料会員登録が必要です。");
	    	  return "/primeregister";
	    	}
    	
    	
    	model.addAttribute("user", user);
    	
    	return "/";
    	
    }
}
