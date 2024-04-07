package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Seat;
import com.example.nagoyameshi.entity.Store;

public interface SeatRepository extends JpaRepository <Seat, Integer> {
	
	public List<Seat> findByStore(Store store);

}
