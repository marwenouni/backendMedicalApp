package com.myapp.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.myapp.demo.Repository.IUserService;
import com.myapp.demo.Repository.UserRepository;
import com.myapp.demo.entity.Produit;
import com.myapp.demo.entity.User;
import com.myapp.demo.exceptions.DuplicationException;
import com.myapp.demo.exceptions.NotFoundException;

@Service
public class UserService implements IUserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired JwtService jwtService;
	
	public User findById(Long id) {
		return userRepo.findById(id).orElseThrow(
				() -> new NotFoundException("Person not found: " + id));
	}
	
	public User findByEmail(String email) {
		
		return userRepo.findByEmail(email).orElseThrow(
				() -> new NotFoundException("Person not found: " + email));
	}

	public User create(User user) {
		checkEmailDuplication( user);
		return userRepo.save(user);
	}
	
	private void checkEmailDuplication(User user) {
		final String email = user.getEmail();
		if (email != null && email.length() > 0) {
			final User u = userRepo.findByEmail(email).orElse(null);
			if (u != null && Objects.equals(u.getEmail(), email)) {
				throw new DuplicationException("Email duplication: " + email);
			}
		}
	}
	
	@Override
	public List<User> getUsers() {
		return userRepo.findAll();
	}
	
	@Override
	public Optional<User> getUserByToken(String token) {
		String email=jwtService.getUsernameFromToken(token.substring(7));
		return userRepo.findByEmail(email);
	}
}
