package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Menu;
import com.example.nagoyameshi.entity.Store;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	public List<Menu> findByStore(Store store);
	
}
