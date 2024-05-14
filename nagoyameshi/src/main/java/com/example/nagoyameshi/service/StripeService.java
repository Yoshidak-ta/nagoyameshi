package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Card;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.CardRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;

	private final UserService userService;
	private final CardRepository cardRepository;
	private final CardService cardService;

	public StripeService(UserService userService,
			CardRepository cardRepository, CardService cardService) {
		this.userService = userService;
		this.cardRepository = cardRepository;
		this.cardService = cardService;
	}

	//	セッションを作成し、Stripeに必要な情報を返す
	public String createStripeSession(HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String domain = "http://localhost:8080";
		SessionCreateParams params = SessionCreateParams.builder()
				.setSuccessUrl(domain + "/?reserved")
				.setCancelUrl(domain + "/")
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPrice("price_1P6VSsG3807R4hwqpWoHOwNd")
								.setQuantity(1L)
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

	//セッションを作成し、Stripeに必要な情報を返す（アップグレード）
	public String updateStripeSession(HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String domain = "http://localhost:8080";
		SessionCreateParams params = SessionCreateParams.builder()
				.setSuccessUrl(domain + "/users?updated")
				.setCancelUrl(domain + "/users")
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setQuantity(1L)
								.setPrice("price_1P6VSsG3807R4hwqpWoHOwNd")
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

	//	追加部分（変更）
	//	セッションからメールアドレスと顧客IDを取得し、CardServiceクラスを介してデータベースに登録
	public void processSessionCompleted(Event event) {
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		optionalStripeObject.ifPresent(stripeObject -> {
			Session session = (Session) stripeObject;
			String customerId = session.getCustomer();
			String subscriptionId = session.getSubscription();

			try {
				Customer customer = Customer.retrieve(customerId);
				String email = customer.getEmail();

				cardService.create(email, customerId, subscriptionId);
				userService.roleUpdate(email);

			} catch (StripeException e) {
				e.printStackTrace();
			}
		});
	}

	//	カスタマーポータルセッションの作成
	public String portalStripeSession(User user, HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		Card card = cardRepository.findByUser(user);
		String customer = card.getCustomerId();
		String domain = "http://localhost:8080";

		com.stripe.param.billingportal.SessionCreateParams params = com.stripe.param.billingportal.SessionCreateParams
				.builder()
				.setReturnUrl(domain + "/users?update")
				.setCustomer(customer)
				.build();

		try {
			com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session
					.create(params);
			return portalSession.getUrl();
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}

	//	サブスクキャンセルデータ削除
	public void delete(Event event) {
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		optionalStripeObject.ifPresent(stripeObject -> {
			Subscription subscription = (Subscription) stripeObject;
			String subscriptionId = subscription.getId();

			cardRepository.deleteBySubscriptionId(subscriptionId);
		});
	}
	//	ここまで

}
