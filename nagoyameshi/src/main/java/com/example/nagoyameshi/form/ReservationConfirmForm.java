package com.example.nagoyameshi.form;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationConfirmForm {

	private Integer storeId;

	private Integer userId;

	private LocalDate visitDate;

	private String visitTime;

	private Integer numberOfPeople;

	private String Other;

}
