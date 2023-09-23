package com.jkngil.pos.users.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jkngil.pos.users.shared.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto userDetails);
	UserDto getUser(String userId);
//	UserDto getUserAlbums(String userId);
	UserDto getUserByEmail(String email);
	UserDto updateUser(String userId, UserDto userDetails);
	void deleteUser(String userId);
	List<UserDto> listUsers(int page, int limit);
}
