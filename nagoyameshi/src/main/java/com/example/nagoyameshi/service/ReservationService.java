package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationConfirmForm;
import com.example.nagoyameshi.repository.ReservationRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Transactional
	public void create(Store store, User user, ReservationConfirmForm reservationConfirmForm) {
		Reservation reservation = new Reservation();

		reservation.setStore(store);
		reservation.setUser(user);
		reservation.setVisitDate(reservationConfirmForm.getVisitDate());
		reservation.setVisitTime(reservationConfirmForm.getVisitTime());
		reservation.setNumberOfPeople(reservationConfirmForm.getNumberOfPeople());
		reservation.setOther(reservationConfirmForm.getOther());

		reservationRepository.save(reservation);
	}

}