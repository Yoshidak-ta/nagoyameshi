package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
	private final UserRepository userRepository;
	
	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false)String keyword, 
			            @RequestParam(name = "age", required = false)Integer age,
			            @RequestParam(name = "job", required = false)String job,
			            @RequestParam(name = "role", required = false)Integer role,
			            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
			            Model model) {
		
		Page<User> userPage;
		
		if(keyword != null && !keyword.isEmpty()) {
			userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword + "%", "%" + keyword + "%", pageable);
		} else if(age != null){
			userPage = userRepository.findByAgeLessThanEqual(age, pageable);
		} else if(job != null && !job.isEmpty()) {
			userPage = userRepository.findByJobLike("%" + job + "%", pageable);
		} else if(role != null) {
			userPage = userRepository.findByRole_id(role , pageable);
		} else {
			userPage = userRepository.findAll(pageable);
		}
		
		
		model.addAttribute("userPage", userPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("age", age);
		model.addAttribute("job", job);
		model.addAttribute("role", role);
		
		return "admin/users/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id")Integer id, Model model) {
		User user = userRepository.getReferenceById(id);
		
		model.addAttribute("user", user);
		
		return "admin/users/show";
	}

}
