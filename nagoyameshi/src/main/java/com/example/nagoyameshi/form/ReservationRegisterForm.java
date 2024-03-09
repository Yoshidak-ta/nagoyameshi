package com.example.nagoyameshi.form;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {

	private Integer storeId;
	
	private Integer userId;
	
	private Date visitDate;
	
	private String visitTime;
	
	private Integer numberOfPeople;
	
	private String Other;

}
