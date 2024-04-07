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

import com.example.nagoyameshi.entity.Menu;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.MenuEditForm;
import com.example.nagoyameshi.form.MenuRegisterForm;
import com.example.nagoyameshi.repository.MenuRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.service.MenuService;

@Controller
@RequestMapping("/admin/stores/{id}/menus")
public class AdminMenuController {
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final MenuService menuService;
	
	public AdminMenuController(MenuRepository menuRepository, StoreRepository storeRepository, MenuService menuService) {
		this.menuRepository = menuRepository;
		this.storeRepository = storeRepository;
		this.menuService = menuService;
	}

	@GetMapping
	private String index(@PathVariable(name = "id")Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		List<Menu> menuList = menuRepository.findByStore(store);
		
		if(!menuList.isEmpty()) {
			Menu menu = menuRepository.getReferenceById(id);
			int price = (int)(menu.getPrice() * 1.1);
			
			model.addAttribute("menu", menu);
			model.addAttribute("price", price);	
		}
		
		model.addAttribute("store", store);
		model.addAttribute("menuList", menuList);
		
		return "admin/menus/index";
	}
	
	@GetMapping("/register")
	public String register(@PathVariable(name = "id")Integer id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		model.addAttribute("store", store);
	    model.addAttribute("menuRegisterForm", new MenuRegisterForm());
		
		return "admin/menus/register";
	}
	
	@PostMapping("/create")
	public String create(@PathVariable(name = "id")Integer id, 
			             @ModelAttribute @Validated MenuRegisterForm menuRegisterForm, 
			             BindingResult bindingResult, 
			             RedirectAttributes redirectAttributes,
			             Model model)  {
		
		Store store = storeRepository.getReferenceById(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			return "admin/menus/register";
		}
		
		menuService.create(store, menuRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "メニューを登録しました。");
		
		return "redirect:/admin/stores/{id}/menus";
	}
	
	@GetMapping("{menuId}/edit")
	public String edit(@PathVariable(name = "id")Integer id,
			           @PathVariable(name = "menuId")Integer menuId,
			           Model model) {
		Store store = storeRepository.getReferenceById(id);
		Menu menu = menuRepository.getReferenceById(menuId);
		String imageName = menu.getImageName();
		MenuEditForm menuEditForm = new MenuEditForm(menu.getId(), menu.getName(), menu.getPrice(), null, menu.getHeading(), menu.getDescription());
		
		model.addAttribute("store", store);
		model.addAttribute("imageName", imageName);
		model.addAttribute("menuEditForm", menuEditForm);
		
		return "admin/menus/edit";
	}
	
	@PostMapping("{menuId}/update")
	public String update(@PathVariable(name = "id")Integer id,
			             @PathVariable(name = "menuId")Integer menuId,
			             @ModelAttribute @Validated MenuEditForm menuEditForm,
			             BindingResult bindingResult,
			             RedirectAttributes redirectAttributes,
			             Model model) {
		Store store = storeRepository.getReferenceById(id);
		Menu menu = menuRepository.getReferenceById(menuId);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("store", store);
			model.addAttribute("menu", menu);
			return "admin/menus/edit";
		}
		
		menuService.update(menuEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "メニューを編集しました。");
		
		return "redirect:/admin/stores/{id}/menus";
	}

	@PostMapping("{menuId}/delete")
	public String delete(@PathVariable(name = "menuId")Integer menuId, RedirectAttributes redirectAttributes) {
		menuRepository.deleteById(menuId);
		
		redirectAttributes.addFlashAttribute("successMessage", "メニューを削除しました。");
		
		return "redirect:/admin/stores/{id}/menus";
	}
}
