package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
}