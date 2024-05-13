package com.example.nagoyameshi.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

	public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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
	public void primecreate(Map<String, String> paymentIntentObject) {
		User user = new User();
		Integer roleId = Integer.valueOf(paymentIntentObject.get("roleId"));

		String name = paymentIntentObject.get("name");
		String furigana = paymentIntentObject.get("furigana");
		Integer age = Integer.valueOf(paymentIntentObject.get("age"));
		String postalCode = paymentIntentObject.get("postalCode");
		String address = paymentIntentObject.get("address");
		String email = paymentIntentObject.get("email");
		String job = paymentIntentObject.get("job");
		String password = paymentIntentObject.get("password");
		Role role = roleRepository.getReferenceById(roleId);

		user.setName(name);
		user.setFurigana(furigana);
		user.setAge(age);
		user.setPostalCode(postalCode);
		user.setAddress(address);
		user.setEmail(email);
		user.setJob(job);
		user.setPassword(password);
		user.setRole(role);
		user.setEnabled(true);

		userRepository.save(user);

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
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private FavoriteRepository favoriteRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Transactional
	public void deleteUserWithAssociations(int userId) {
		// 関連するテーブルから先にデータを削除
		verificationTokenRepository.deleteByUser_id(userId);
		reviewRepository.deleteByUser_id(userId);
		favoriteRepository.deleteByUser_id(userId);
		reservationRepository.deleteByUser_id(userId);
		// 最後にユーザー自身を削除
		userRepository.deleteById(userId);
	}

}
