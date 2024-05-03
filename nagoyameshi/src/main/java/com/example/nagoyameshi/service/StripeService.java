package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.form.SignupConfirmForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;

	private final AuthService authService;

	public StripeService(AuthService authService) {
		this.authService = authService;
	}

	//	セッションを作成し、Stripeに必要な情報を返す
	public String createStripeSession(SignupConfirmForm signupConfirmForm, HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String requestUrl = new String(httpServletRequest.getRequestURL());
		String domain = "http://localhost:8080/";
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPrice("price_1P6VSsG3807R4hwqpWoHOwNd")
								.setQuantity(1L)
								.build())
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(domain + "/users")
				.setCancelUrl(domain + "/signup")
				.setPaymentIntentData(
						SessionCreateParams.PaymentIntentData.builder()
								.putMetadata("name", signupConfirmForm.getName())
								.putMetadata("furigana", signupConfirmForm.getFurigana())
								.putMetadata("age", signupConfirmForm.getAge().toString())
								.putMetadata("postalCode", signupConfirmForm.getPostalCode())
								.putMetadata("address", signupConfirmForm.getAddress())
								.putMetadata("password", signupConfirmForm.getPassword())
								.putMetadata("roleId", signupConfirmForm.getRoleId().toString())
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
				authService.create(paymentIntentObject);
			} catch (StripeException e) {
				e.printStackTrace();
			}
		});
	}

}
