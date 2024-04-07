package com.example.nagoyameshi.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "メニュー名を入力してください。")
	private String name;
	
	@NotNull(message = "本体価格（税抜き価格）を入力してください。")
	private Integer price;
	
	private MultipartFile imageFile;

	@NotBlank(message = "メニュー説明の見出しを入力してください。")
	private String heading;
	
	@NotBlank(message = "メニュー説明を入力してください。")
	@Length(min = 50, max = 200, message = "50文字以上200文字以下で記入してください。")
    private String description;

}