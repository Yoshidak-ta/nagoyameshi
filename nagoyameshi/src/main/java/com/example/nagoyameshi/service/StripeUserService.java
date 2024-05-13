package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserConfirmForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeUserService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;

	private final UserService userService;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public StripeUserService(UserService userService, RoleRepository roleRepository, UserRepository userRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	//	セッションを作成し、Stripeに必要な情報を返す
	public String createStripeSession(UserConfirmForm userConfirmForm, HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String requestUrl = new String(httpServletRequest.getRequestURL());
		String age = (userConfirmForm.getAge() != null) ? userConfirmForm.getAge().toString() : "デフォルト値または空文字列";
		String roleId = (userConfirmForm.getRoleId() != null) ? userConfirmForm.getRoleId().toString()
				: "デフォルト値または空文字列";
		String domain = "http://localhost:8080";
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPrice("price_1P6VSsG3807R4hwqpWoHOwNd")
								.setQuantity(1L)
								.build())
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setSuccessUrl(domain + "/users")
				.setCancelUrl(domain + "/users/edit")
				.setSubscriptionData(
						SessionCreateParams.SubscriptionData.builder()
								.putMetadata("name", userConfirmForm.getName())
								.putMetadata("furigana", userConfirmForm.getFurigana())
								.putMetadata("age", age)
								.putMetadata("postalCode", userConfirmForm.getPostalCode())
								.putMetadata("address", userConfirmForm.getAddress())
								.putMetadata("roleId", roleId)
								.build())
				.build();
		try {
			Session session = Session.create(params);
			return session.getId();
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void processSessionCompleted(Event event) {
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		optionalStripeObject.ifPresent(stripeObject -> {
			Session session = (Session) stripeObject;
			SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();

			try {
				session = Session.retrieve(session.getId(), params, null);
				Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
				userService.primeUpdate(paymentIntentObject);
				User user = userRepository.findByName(paymentIntentObject.get("name"));
				Integer roleId = Integer.valueOf(paymentIntentObject.get("roleId"));
				userService.roleUpdate(user, roleId);

			} catch (StripeException e) {
				e.printStackTrace();
			}
		});
	}

}
