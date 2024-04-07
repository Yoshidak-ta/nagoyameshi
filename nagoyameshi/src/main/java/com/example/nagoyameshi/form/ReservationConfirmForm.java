package com.example.nagoyameshi.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationConfirmForm {

	private Integer storeId;
	
	private Integer userId;
	
	private String visitDate;
	
	private String visitTime;
	
	private Integer numberOfPeople;
	
	private String Other;

}