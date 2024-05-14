package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);

	public User findByName(String userName);

	public Page<User> findByRole_id(Integer role, Pageable pageable);

	public Page<User> findByNameLike(String keyword, Pageable pageable);

	public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);

	public Page<User> findByAgeLessThanEqual(Integer age, Pageable pageable);

	public Page<User> findByJobLike(String job, Pageable pageable);

	public Page<User> findByRoleLessThanEqual(Integer role, Pageable pageable);

}
