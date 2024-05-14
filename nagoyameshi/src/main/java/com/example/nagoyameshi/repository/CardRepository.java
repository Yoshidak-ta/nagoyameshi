package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Card;
import com.example.nagoyameshi.entity.User;

public interface CardRepository extends JpaRepository<Card, Integer> {
	public Card findByUser(User user);

	public Card findBySubscriptionId(String subscrioptionId);

	@Transactional
	public Integer deleteBySubscriptionId(String subscriptionId);

}
