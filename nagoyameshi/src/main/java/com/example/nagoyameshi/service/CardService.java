package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Card;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.CardRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class CardService {
	private final CardRepository cardRepository;
	private final UserRepository userRepository;

	public CardService(CardRepository cardRepository, UserRepository userRepository) {
		this.cardRepository = cardRepository;
		this.userRepository = userRepository;
	}

	//カード情報登録機能
	@Transactional
	public Card create(String email, String customerId, String subscriptionId) {
		Card card = new Card();

		User user = userRepository.findByEmail(email);

		card.setUser(user);
		card.setCustomerId(customerId);
		card.setSubscriptionId(subscriptionId);

		return cardRepository.save(card);
	}

	//カード情報編集機能
	@Transactional
	public Card update(String email, String customerId) {
		User user = userRepository.findByEmail(email);
		Card card = cardRepository.findByUser(user);

		card.setUser(user);
		card.setCustomerId(customerId);

		return cardRepository.save(card);
	}

}
