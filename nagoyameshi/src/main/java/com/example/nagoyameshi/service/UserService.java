package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.CardRepository;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final CardRepository cardRepository;

	public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
			CardRepository cardRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.cardRepository = cardRepository;

	}

	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role_general = roleRepository.findByName("ROLE_GENERAL");

		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setAge(signupForm.getAge());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setEmail(signupForm.getEmail());
		user.setJob(signupForm.getJob());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setRole(role_general);
		user.setEnabled(false);

		return userRepository.save(user);

	}

	@Transactional
	public void update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());

		user.setName(userEditForm.getName());
		user.setFurigana(userEditForm.getFurigana());
		user.setAge(userEditForm.getAge());
		user.setPostalCode(userEditForm.getPostalCode());
		user.setAddress(userEditForm.getAddress());
		user.setEmail(userEditForm.getEmail());
		user.setJob(userEditForm.getJob());

		userRepository.save(user);

	}

	//	メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	}

	//	パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	//	ユーザーを有効にする
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}

	//	メールアドレスが変更されたかチェック
	public boolean isEmailChanged(UserEditForm userEditForm) {
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		return !userEditForm.getEmail().equals(currentUser.getEmail());
	}

	//	有料会員のroleIdを2にする
	@Transactional
	public void roleUpdate(String email) {
		User user = userRepository.findByEmail(email);
		Role role = roleRepository.getReferenceById(2);

		user.setRole(role);

		userRepository.save(user);
	}

	//	@Transactional
	//	public void premissionForeignKey() {
	//		String sqlVerificationTokens = "ALTER TABLE verification_tokens MODIFY user_id INT NULL;";
	//		String sqlReservations = "ALTER TABLE reservations MODIFY user_id INT NULL;";
	//		String sqlReviews = "ALTER TABLE reviews MODIFY user_id INT NULL;";
	//		String sqlFavorites = "ALTER TABLE favorites MODIFY user_id INT NULL;";
	//
	//		jdbcTemplate.execute(sqlVerificationTokens);
	//		jdbcTemplate.execute(sqlReservations);
	//		jdbcTemplate.execute(sqlReviews);
	//		jdbcTemplate.execute(sqlFavorites);
	//	}
	//
	//	@Transactional
	//	public void dropForeignKey() {
	//		String sqlVerificationTokens = "ALTER TABLE verification_tokens DROP FOREIGN KEY verification_tokens_ibfk_2;";
	//		String sqlReservations = "ALTER TABLE reservations DROP FOREIGN KEY reservations_ibfk_2;";
	//		String sqlReviews = "ALTER TABLE reviews DROP FOREIGN KEY reviews_ibfk_2;";
	//		String sqlFavorite = "ALTER TABLE favorites DROP FOREIGN KEY favorites_ibfk_2;";
	//
	//		jdbcTemplate.execute(sqlVerificationTokens);
	//		jdbcTemplate.execute(sqlReservations);
	//		jdbcTemplate.execute(sqlReviews);
	//		jdbcTemplate.execute(sqlFavorite);
	//	}
	//
	//	@Transactional
	//	public void checkForeignKey() {
	//		String sqlVerifications = "ALTER TABLE verification_tokens ADD CONSTRAINT verification_tokens_ibfk_1 FOREIGN KEY (user_id) REFERENCES users(id);";
	//		String sqlReservations = "ALTER TABLE reservations ADD CONSTRAINT reservations_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";
	//		String sqlReviews = "ALTER TABLE reviews ADD CONSTRAINT reviews_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";
	//		String sqlFavorite = "ALTER TABLE favorites ADD CONSTRAINT favorites_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";
	//
	//		jdbcTemplate.execute(sqlVerifications);
	//		jdbcTemplate.execute(sqlReservations);
	//		jdbcTemplate.execute(sqlReviews);
	//		jdbcTemplate.execute(sqlFavorite);
	//	}
	//
	//	@Transactional
	//	public void dropUserId(User user) {
	//		Integer userId = user.getId();
	//		userRepository.deleteById(userId);
	//		verificationTokenRepository.deleteByUser(user);
	//		reservationRepository.deleteByUser(user);
	//		reviewRepository.deleteByUser(user);
	//		favoriteRepository.deleteByUser(user);
	//	}
	//
	//	//	外部キーを一次的削除に変更
	//	public void dropForeignKey() {
	//		String sql = "SET FOREIGN_KEY_CHECKS = 0";
	//		jdbcTemplate.execute(sql);
	//	}
	//
	//	//外部キー一次的削除を解除
	//	public void checkForeignKey() {
	//		String sql = "SET FOREIGN_KEY_CHECKS = 1";
	//		jdbcTemplate.execute(sql);
	//	}

}
