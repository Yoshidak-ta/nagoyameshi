package com.example.nagoyameshi.controller;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationConfirmForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

@Controller
@RequestMapping("/stores/{id}/reservations")
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final StoreRepository storeRepository;
	private final ReservationService reservationService;
	
	public ReservationController(ReservationRepository reservationRepository, StoreRepository storeRepository, ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.storeRepository = storeRepository;
		this.reservationService = reservationService;
	}
	
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		
		return "reservations/index";
	}
	
	@GetMapping("/register")
	public String register(@PathVariable(name = "id")Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		model.addAttribute("store", store);
		model.addAttribute("reservationRegisterForm", new ReservationRegisterForm());
		
		return "reservations/register";
	}
	
	@GetMapping("/input")
	public String input(@PathVariable(name = "id")Integer id,
			            @ModelAttribute @Validated ReservationRegisterForm reservationRegisterForm,
			            BindingResult bindingResult,
			            RedirectAttributes redirectAttributes,
			            Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			model.addAttribute("errorMessage", "予約内容に不備があります。");
			return "reservations/register";
		}
		
		redirectAttributes.addFlashAttribute("reservationRegisterForm", reservationRegisterForm);
		
		return "redirect:/stores/{id}/reservations/confirm";
		
	}
	
	@GetMapping("/confirm")
	public String confirm(@PathVariable(name = "id")Integer id,
			              @ModelAttribute ReservationRegisterForm reservationRegisterForm,
			              @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			              Model model) {
		
		Store store = storeRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		Date visitDate = reservationRegisterForm.getVisitDate();
		
		ReservationConfirmForm reservationConfirmForm = new ReservationConfirmForm(store.getId(), user.getId(), visitDate, reservationRegisterForm.getVisitTime(), reservationRegisterForm.getNumberOfPeople(), reservationRegisterForm.getOther());
		
		
		model.addAttribute("store", store);
		model.addAttribute("reservationConfirmForm", reservationConfirmForm);
		
		return "reservations/confirm";
	}
	
	
	@PostMapping("/create")
	  public String create(@ModelAttribute ReservationConfirmForm reservationConfirmForm) {
		  reservationService.create(reservationConfirmForm);
		  
		  return "redirect:/reservations?reserved";
	  }

}
