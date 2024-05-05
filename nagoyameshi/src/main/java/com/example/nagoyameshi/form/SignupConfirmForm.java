package com.example.nagoyameshi.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupConfirmForm {
	private String name;

	private String furigana;

	private Integer age;

	private String postalCode;

	private String address;

	private String email;

	private String job;

	private String password;

	private Integer roleId;

}