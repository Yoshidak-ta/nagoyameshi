package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.form.ReservationConfirmForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	private final ReservationService reservationService;
	
	public StripeService(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	 // セッションを作成し、Stripeに必要な情報を返す
    public String createStripeSession(String storeName, ReservationConfirmForm reservationConfirmForm, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey;
        String requestUrl = new String(httpServletRequest.getRequestURL());
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(storeName)
                                        .build())
                                .setCurrency("jpy")                                
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(requestUrl.replaceAll("/primeregister", "") + "/primeregister?reserved")
                .setCancelUrl(requestUrl.replace("/primeregister", ""))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("storeId", reservationConfirmForm.getStoreId().toString())
                        .putMetadata("userId", reservationConfirmForm.getUserId().toString())
                        .putMetadata("visitDate", reservationConfirmForm.getVisitDate())
                        .putMetadata("visitTime", reservationConfirmForm.getVisitTime())
                        .putMetadata("numberOfPeole", reservationConfirmForm.getNumberOfPeople().toString())
                        .putMetadata("other", reservationConfirmForm.getOther())
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
    


}