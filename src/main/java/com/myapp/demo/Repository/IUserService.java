package com.myapp.demo.Repository;

import java.util.List;
import java.util.Optional;

import com.myapp.demo.entity.User;

public interface IUserService {

	List<User> getUsers();

	public Optional<User> getUserByToken(String token);

}
