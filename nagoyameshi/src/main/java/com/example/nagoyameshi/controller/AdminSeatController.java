package com.example.nagoyameshi.controller;

import java.util.List;

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

import com.example.nagoyameshi.entity.Seat;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.SeatEditForm;
import com.example.nagoyameshi.form.SeatRegisterForm;
import com.example.nagoyameshi.repository.SeatRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.service.SeatService;

@Controller
@RequestMapping("admin/stores/{id}/seats")
public class AdminSeatController {
	private final SeatRepository seatRepository;
	private final StoreRepository storeRepository;
	private final SeatService seatService;
	
	public AdminSeatController(SeatRepository seatRepository, StoreRepository storeRepository, SeatService seatService) {
		this.seatRepository = seatRepository;
		this.storeRepository = storeRepository;
		this.seatService = seatService;
	}
	
	@GetMapping
	public String index(@PathVariable(name = "id")Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		List<Seat> seatList = seatRepository.findByStore(store);
		
		 if(!seatList.isEmpty()) {
			 Seat seat = seatRepository.getReferenceById(id);
			
			 model.addAttribute("seat", seat);
		 }
		
		model.addAttribute("store", store);
		model.addAttribute("seatList", seatList);
		
		return "admin/seats/index";
	}
	
	@GetMapping("/register")
	public String register(@PathVariable(name = "id")Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		model.addAttribute("store", store);
		model.addAttribute("seatRegisterForm", new SeatRegisterForm());
		
		return "admin/seats/register";
	}
	
	@PostMapping("/create")
	public String create(@PathVariable(name = "id")Integer id,
			             @ModelAttribute @Validated SeatRegisterForm seatRegisterForm,
			             BindingResult bindingResult,
			             RedirectAttributes redirectAttributes,
			             Model model) {
		
		Store store = storeRepository.getReferenceById(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			
			return "admin/seats/register";
		}
		
		seatService.create(store, seatRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "シートを登録しました。");
		
		return "redirect:/admin/stores/{id}/seats";
	}
	
	@GetMapping("{seatId}/edit")
	public String edit(@PathVariable(name = "id")Integer id,
			           @PathVariable(name = "seatId")Integer seatId,
			           Model model) {
		Store store = storeRepository.getReferenceById(id);
		Seat seat = seatRepository.getReferenceById(seatId);
		String imageName = seat.getImageName();
		SeatEditForm seatEditForm = new SeatEditForm(seat.getId(), null, seat.getSeatOfNumber(), seat.getCounter(), seat.getPrivateRoom());
		
		model.addAttribute("store", store);
		model.addAttribute("imageName", imageName);
		model.addAttribute("seatEditForm", seatEditForm);
		
		return "admin/seats/edit";
	
	}
	
	@PostMapping("{seatId}/update")
	public String update(@PathVariable(name = "id")Integer id,
			             @PathVariable(name = "seatId")Integer seatId,
			             @ModelAttribute @Validated SeatEditForm seatEditForm,
			             BindingResult bindingResult,
			             RedirectAttributes redirectAttributes,
			             Model model) {
		Store store = storeRepository.getReferenceById(id);
		Seat seat = seatRepository.getReferenceById(seatId);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			model.addAttribute("seat", seat);
			return "admin/seats/edit";
		}
		
		seatService.update(seatEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "座席を編集しました。");
		
		return "redirect:/admin/stores/{id}/seats";
		
	}
	
	@PostMapping("{seatId}/delete")
	public String delete(@PathVariable(name = "seatId")Integer seatId, RedirectAttributes redirectAttributes) {
		seatRepository.deleteById(seatId);
		
		redirectAttributes.addFlashAttribute("successMessage", "座席を削除しました");
		
		return "redirect:/admin/stores/{id}/seats";
	}

}
