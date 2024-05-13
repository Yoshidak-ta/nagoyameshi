package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	public Page<Review> findByStoreOrderByCreatedAtDesc(Store store, Pageable pageable);

	public long countByStore(Store store);

	public Review findByUserAndStore(User user, Store store);

	public List<Review> findByStore(Store store);

	public Review deleteByUser(User user);

	@Query("SELECT AVG(r.score) FROM Review r WHERE r.store = :store")
	Double findAverageScoreByStore(@Param("store") Store store);

}
