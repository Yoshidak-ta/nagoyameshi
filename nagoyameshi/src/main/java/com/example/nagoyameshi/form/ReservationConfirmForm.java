package com.example.nagoyameshi.form;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationConfirmForm {

	private Integer storeId;
	
	private Integer userId;
	
	private Date visitDate;
	
	private String visitTime;
	
	private Integer numberOfPeople;
	
	private String Other;

}
