package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationConfirmForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	
	public ReservationService(ReservationRepository reservationRepository, StoreRepository storeRepository, UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void create(ReservationConfirmForm reservationConfirmForm) {
		Reservation reservation = new Reservation();
		Store store = storeRepository.getReferenceById(reservationConfirmForm.getStoreId());
		User user = userRepository.getReferenceById(reservationConfirmForm.getUserId());
		
		reservation.setStore(store);
		reservation.setUser(user);
		reservation.setVisitDate(reservationConfirmForm.getVisitDate());
		reservation.setVisitTime(reservationConfirmForm.getVisitTime());
		reservation.setNumberOfPeople(reservationConfirmForm.getNumberOfPeople());
		reservation.setOther(reservationConfirmForm.getOther());
		
		reservationRepository.save(reservation);
	}
	
}