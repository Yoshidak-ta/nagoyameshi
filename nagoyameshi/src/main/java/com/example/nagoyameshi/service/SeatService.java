package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Seat;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.SeatEditForm;
import com.example.nagoyameshi.form.SeatRegisterForm;
import com.example.nagoyameshi.repository.SeatRepository;

@Service
public class SeatService {
	private final SeatRepository seatRepository;

	public SeatService(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

	@Transactional
	public void create(Store store, SeatRegisterForm seatRegisterForm) {
		Seat seat = new Seat();
		MultipartFile imageFile = seatRegisterForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/seats/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			seat.setImageName(hashedImageName);
		}

		seat.setStore(store);
		seat.setSeatOfNumber(seatRegisterForm.getSeatOfNumber());
		seat.setCounter(seatRegisterForm.getCounter());
		seat.setPrivateRoom(seatRegisterForm.getPrivateRoom());

		seatRepository.save(seat);
	}

	@Transactional
	public void update(SeatEditForm seatEditForm) {
		Seat seat = seatRepository.getReferenceById(seatEditForm.getId());
		MultipartFile imageFile = seatEditForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/images/seats/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			seat.setImageName(hashedImageName);
		}

		seat.setSeatOfNumber(seatEditForm.getSeatOfNumber());
		seat.setCounter(seatEditForm.getCounter());
		seat.setPrivateRoom(seatEditForm.getPrivateRoom());

		seatRepository.save(seat);

	}

	//	UUIDを使って生成したファイル名を渡す
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
