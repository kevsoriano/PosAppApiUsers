package com.jkngil.pos.api.users.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

	@GetMapping
	public String getUser() {
		return "";
	}
	
	@GetMapping
	public String getUsers() {
		return "";
	}
	
	@PostMapping
	public String createUser() {
		return "";
	}
	
	@PutMapping
	public String updateUser() {
		return "";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "";
	}
}
