package com.rozmer.service.repo;

import com.rozmer.service.entities.GuestUser;
import com.rozmer.service.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GuestUserRepository extends CrudRepository<GuestUser,Long> {

	public GuestUser findByGuestEmail(String email);
	
}
