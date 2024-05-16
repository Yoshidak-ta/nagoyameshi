package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.SubscriptionService;
import com.example.nagoyameshi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	private final StripeService stripeService;
	private final SubscriptionService subscriptionService;

	public UserController(UserRepository userRepository, UserService userService,
			StripeService stripeService, SubscriptionService subscriptionService) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.stripeService = stripeService;
		this.subscriptionService = subscriptionService;
	}

	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, HttpServletRequest httpServletRequest,
			Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		subscriptionService.upgradePrimeUser(user.getName());

		String sessionId = stripeService.createStripeSession(httpServletRequest);

		model.addAttribute("user", user);
		model.addAttribute("sessionId", sessionId);

		return "users/index";
	}

	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(), user.getAge(),
				user.getPostalCode(), user.getAddress(), user.getEmail(), user.getJob());

		model.addAttribute("userEditForm", userEditForm);

		return "users/edit";
	}

	@GetMapping("/subscription")
	public String subscription(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model,
			HttpServletRequest httpServletRequest) {
		User user = userDetailsImpl.getUser();

		String portalSessionUrl = stripeService.portalStripeSession(user, httpServletRequest);

		model.addAttribute("portalSessionUrl", portalSessionUrl);

		return "users/subscription";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		//		メールアドレスが変更されておりかつ登録済みであればBindingRsultオブジェクトにエラー内容を追加
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "既に登録済みのメールアドレスです。");
			bindingResult.addError(fieldError);
		}

		if (bindingResult.hasErrors()) {
			return "users/edit";
		}

		userService.update(userEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		return "redirect:/users";
	}

	@PostMapping("/delete")
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());

		userService.premissionForeignKey();
		userService.dropForeignKey();
		userService.dropUserId(user);
		userService.checkForeignKey();

		redirectAttributes.addFlashAttribute("successMessage", "退会しました。");

		return "redirect:/";

	}

}
