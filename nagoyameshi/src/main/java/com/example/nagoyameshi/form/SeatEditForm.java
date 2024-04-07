package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatEditForm {
	@NotNull
	private Integer id;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "総座席数とその内容を入力してください。")
	private String seatOfNumber;
	
	@NotBlank(message = "カウンター席の有無を選択してください。")
	private String counter;
	
	@NotBlank(message = "個室・半個室の有無を選択してください。")
	private String privateRoom;

}