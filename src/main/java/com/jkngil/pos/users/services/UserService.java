package com.jkngil.pos.users.services;

import java.util.List;

import com.jkngil.pos.users.shared.UserDto;

public interface UserService {
	UserDto createUser(UserDto userDetails);
	UserDto getUser(String userId);
	UserDto updateUser(String userId, UserDto userDetails);
	void deleteUser(String userId);
	List<UserDto> listUsers(int page, int limit);
}
