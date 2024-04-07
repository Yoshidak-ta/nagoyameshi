package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeatRegisterForm {
	private MultipartFile imageFile;
	
	@NotBlank(message = "総座席数とその内容を入力してください。")
	private String seatOfNumber;
	
	@NotBlank(message = "カウンター席数を有無を選択してください。")
	private String counter;
	
	@NotBlank(message = "個室・半個室の有無を選択してください。")
	private String privateRoom;

}
