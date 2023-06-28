package com.rozmer.service.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rozmer.service.entities.User;

public interface UserRepository extends CrudRepository<User,Long> {

//    @Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.resetPasswordToken = ?1")
	public User findByToken(String resetPasswordToken);
	
	@Query( value = "SELECT * FROM users us WHERE us.verification_code = ?1", nativeQuery = true)
	public User findByVerificationCode(String code);

	public User findByResetPasswordToken(String token);
	
}
