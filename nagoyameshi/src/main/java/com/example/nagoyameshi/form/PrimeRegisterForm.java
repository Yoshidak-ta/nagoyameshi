package com.example.nagoyameshi.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrimeRegisterForm {
	private Integer userId;

	@NotNull(message = "クレジットカード番号を入力してください。")
	@Length(max = 16, message = "16桁で入力してください。")
	private Integer cardNumber;

	@NotBlank(message = "カード名義をローマ字（大文字）で入力してください。")
	private String meigi;

	@NotNull(message = "有効期限を入力してください。")
	private Integer expiry;

	@NotNull(message = "セキュリティコードを入力してください。")
	private Integer securityCode;

	private Integer amount;

}
