package com.example.nagoyameshi.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupConfirmForm;
import com.example.nagoyameshi.form.UserConfirmForm;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class UserService {
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;
	private final ReviewRepository reviewRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final FavoriteRepository favoriteRepository;

	public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder,
			ReservationRepository reservationRepository, ReviewRepository reviewRepository,
			VerificationTokenRepository verificationTokenRepository, FavoriteRepository favoriteRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.reservationRepository = reservationRepository;
		this.reviewRepository = reviewRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.favoriteRepository = favoriteRepository;

	}

	@Transactional
	public User create(SignupConfirmForm signupConfirmForm) {
		User user = new User();
		Role role_general = roleRepository.findByName("ROLE_GENERAL");

		user.setName(signupConfirmForm.getName());
		user.setFurigana(signupConfirmForm.getFurigana());
		user.setAge(signupConfirmForm.getAge());
		user.setPostalCode(signupConfirmForm.getPostalCode());
		user.setAddress(signupConfirmForm.getAddress());
		user.setEmail(signupConfirmForm.getEmail());
		user.setJob(signupConfirmForm.getJob());
		user.setPassword(passwordEncoder.encode(signupConfirmForm.getPassword()));
		user.setRole(role_general);
		user.setEnabled(false);

		return userRepository.save(user);

	}

	@Transactional
	public void update(UserConfirmForm userConfirmForm) {
		User user = userRepository.getReferenceById(userConfirmForm.getId());
		Role role = roleRepository.getReferenceById(userConfirmForm.getRoleId());

		user.setName(userConfirmForm.getName());
		user.setFurigana(userConfirmForm.getFurigana());
		user.setAge(userConfirmForm.getAge());
		user.setPostalCode(userConfirmForm.getPostalCode());
		user.setAddress(userConfirmForm.getAddress());
		user.setEmail(userConfirmForm.getEmail());
		user.setJob(userConfirmForm.getJob());
		user.setRole(role);

		userRepository.save(user);

	}

	@Transactional
	public void primeUpdate(Map<String, String> paymentIntentObject) {
		Integer roleId = Integer.valueOf(paymentIntentObject.get("roleId"));

		Integer id = Integer.valueOf(paymentIntentObject.get("id"));
		String name = paymentIntentObject.get("name");
		String furigana = paymentIntentObject.get("furigana");
		Integer age = Integer.valueOf(paymentIntentObject.get("age"));
		String postalCode = paymentIntentObject.get("postalCode");
		String address = paymentIntentObject.get("address");
		String email = paymentIntentObject.get("email");
		String job = paymentIntentObject.get("job");
		Role role = roleRepository.getReferenceById(roleId);

		User user = userRepository.getReferenceById(id);

		user.setName(name);
		user.setFurigana(furigana);
		user.setAge(age);
		user.setPostalCode(postalCode);
		user.setAddress(address);
		user.setEmail(email);
		user.setJob(job);
		user.setRole(role);
		user.setEnabled(true);

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
	public boolean isEmailChanged(UserConfirmForm userConfirmForm) {
		User currentUser = userRepository.getReferenceById(userConfirmForm.getId());
		return !userConfirmForm.getEmail().equals(currentUser.getEmail());
	}

	//	有料会員のroleIdを2にする
	public void roleUpdate(User user, Integer roleId) {
		roleId = 2;
		Role role = roleRepository.getReferenceById(roleId);

		user.setRole(role);

		userRepository.save(user);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void premissionForeignKey() {
		String sqlVerificationTokens = "ALTER TABLE verification_tokens MODIFY user_id INT NULL;";
		String sqlReservations = "ALTER TABLE reservations MODIFY user_id INT NULL;";
		String sqlReviews = "ALTER TABLE reviews MODIFY user_id INT NULL;";
		String sqlFavorites = "ALTER TABLE favorites MODIFY user_id INT NULL;";

		jdbcTemplate.execute(sqlVerificationTokens);
		jdbcTemplate.execute(sqlReservations);
		jdbcTemplate.execute(sqlReviews);
		jdbcTemplate.execute(sqlFavorites);
	}

	@Transactional
	public void dropForeignKey() {
		String sqlVerifications = "ALTER TABLE verification_tokens DROP FOREIGN KEY verification_tokens_ibfk_1;";
		String sqlReservations = "ALTER TABLE reservations DROP FOREIGN KEY reservations_ibfk_2;";
		String sqlReviews = "ALTER TABLE reviews DROP FOREIGN KEY reviews_ibfk_2;";
		String sqlFavorite = "ALTER TABLE favorites DROP FOREIGN KEY favorites_ibfk_2;";

		jdbcTemplate.execute(sqlVerifications);
		jdbcTemplate.execute(sqlReservations);
		jdbcTemplate.execute(sqlReviews);
		jdbcTemplate.execute(sqlFavorite);
	}

	@Transactional
	public void checkForeignKey() {
		String sqlVerifications = "ALTER TABLE verification_tokens ADD CONSTRAINT verification_tokens_ibfk_1 FOREIGN KEY (user_id) REFERENCES users(id);";
		String sqlReservations = "ALTER TABLE reservations ADD CONSTRAINT reservations_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";
		String sqlReviews = "ALTER TABLE reviews ADD CONSTRAINT reviews_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";
		String sqlFavorite = "ALTER TABLE favorites ADD CONSTRAINT favorites_ibfk_2 FOREIGN KEY (user_id) REFERENCES users(id);";

		jdbcTemplate.execute(sqlVerifications);
		jdbcTemplate.execute(sqlReservations);
		jdbcTemplate.execute(sqlReviews);
		jdbcTemplate.execute(sqlFavorite);
	}

	@Transactional
	public void dropUserId(User user) {
		Integer userId = user.getId();
		userRepository.deleteById(userId);
		verificationTokenRepository.deleteByUser(user);
		reservationRepository.deleteByUser(user);
		reviewRepository.deleteByUser(user);
		favoriteRepository.deleteByUser(user);
	}

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
