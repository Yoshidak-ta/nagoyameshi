package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Menu;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.MenuRepository;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
public class HomeController {
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	
	public HomeController(StoreRepository storeRepository, MenuRepository menuRepository) {
		this.storeRepository = storeRepository;
		this.menuRepository = menuRepository;
	}
	
    @GetMapping("/")
    public String index(Model model) {
    	List<Store> newStores = storeRepository.findTop10ByOrderByCreatedAtDesc();
    	List<Menu> menu = menuRepository.findAll();
    	
    	
    	model.addAttribute("newStores", newStores);
    	model.addAttribute("menu", menu);
    	
    	return "index";
  }
}
