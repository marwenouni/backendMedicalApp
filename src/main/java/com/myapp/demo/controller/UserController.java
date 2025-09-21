package com.myapp.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.demo.Repository.IUserService;
import com.myapp.demo.entity.User;

@RestController
@RequestMapping("api/user")
@CrossOrigin()
public class UserController {
	@Autowired
	private IUserService userService;
	
//	@GetMapping
//	List<User> getUsers(){
//		return userService.getUsers();
//	}
	
	@GetMapping
	Optional<User> getUser(@RequestHeader String Authorization){
		String e=Authorization;
		return userService.getUserByToken(Authorization);
	}

}
