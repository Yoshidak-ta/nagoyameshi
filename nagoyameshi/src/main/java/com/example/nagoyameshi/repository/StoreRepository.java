package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
	public Page<Store> findByNameLike(String keyword, Pageable pageable);

	public Page<Store> findByNameLikeOrCategory_SubNameLike(String nameKeyword, String categoryKeyword,
			Pageable pageable);

	public Page<Store> findByCategory_SubNameLike(String category, Pageable pageable);

	public Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable);

	public List<Store> findTop10ByOrderByCreatedAtDesc();
}
