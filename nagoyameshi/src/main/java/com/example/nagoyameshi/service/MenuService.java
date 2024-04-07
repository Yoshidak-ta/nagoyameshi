package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Menu;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.MenuEditForm;
import com.example.nagoyameshi.form.MenuRegisterForm;
import com.example.nagoyameshi.repository.MenuRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	
	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
	
	@Transactional
	public void create(Store store, MenuRegisterForm menuRegisterForm) {
		Menu menu = new Menu();
		MultipartFile imageFile = menuRegisterForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/menus/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			menu.setImageName(hashedImageName);
		}
		
		menu.setStore(store);
		menu.setName(menuRegisterForm.getName());
		menu.setPrice(menuRegisterForm.getPrice());
		menu.setHeading(menuRegisterForm.getHeading());
		menu.setDescription(menuRegisterForm.getDescription());
		
		menuRepository.save(menu);
		
	}
	
	@Transactional
	public void update(MenuEditForm menuEditForm) {
		Menu menu = menuRepository.getReferenceById(menuEditForm.getId());
		MultipartFile imageFile = menuEditForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/menus/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			menu.setImageName(hashedImageName);
		}
		
		menu.setName(menuEditForm.getName());
		menu.setPrice(menuEditForm.getPrice());
		menu.setHeading(menuEditForm.getHeading());
		menu.setDescription(menuEditForm.getDescription());
		
		menuRepository.save(menu);
	}
	
//	UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}
	
//	画像ファイルを指定したファイルにコピー
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}


