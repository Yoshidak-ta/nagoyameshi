package com.example.nagoyameshi.form;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRegisterForm {
	private Integer storeId;
	
	private Integer userId;;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;
	
	@NotBlank(message = "来店時刻を選択してください。")
	private String visitTime;
	
	@NotNull(message = "来店人数を入力してください。")
	@Min(value = 1, message = "来店人数は1人以上に設定にしてください。")
	private Integer numberOfPeople;
	
	private String other;
}
