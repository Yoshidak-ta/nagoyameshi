package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
//	お気に入り登録・追加
	@Transactional
	public void register(Store store, User user) {
		Favorite favorite = new Favorite();
		
		favorite.setStore(store);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	}
	
	public boolean favoriteJudge(Store store, User user) {
		Favorite favorite = favoriteRepository.findByStoreAndUser(store, user);
		
		return favorite != null;
	}

}
