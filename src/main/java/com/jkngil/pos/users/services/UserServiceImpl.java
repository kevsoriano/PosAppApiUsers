package com.jkngil.pos.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jkngil.pos.users.data.UserEntity;
import com.jkngil.pos.users.data.UserRepository;
import com.jkngil.pos.users.shared.AddressDto;
import com.jkngil.pos.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		for(int i = 0; i<userDetails.getAddresses().size();i++) {
			AddressDto address = userDetails.getAddresses().get(i);
			address.setUserDetails(userDetails);
			address.setAddressId(UUID.randomUUID().toString());
			userDetails.getAddresses().set(i, address);
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setUserId(UUID.randomUUID().toString());
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		UserEntity savedUser = userRepository.save(userEntity);
		
		UserDto returnValue = modelMapper.map(savedUser, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDto getUser(String userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDetails) {
		List<AddressDto> addresses = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		userDto.setFirstName(userDetails.getFirstName());
		userDto.setLastName(userDetails.getLastName());
		for(int i = 0; i<userDetails.getAddresses().size();i++) {
			AddressDto address = userDetails.getAddresses().get(i);
			address.setAddressId(userEntity.getAddresses().get(i).getAddressId());
			address.setId(userEntity.getAddresses().get(i).getId());
			address.setUserDetails(userDto.getAddresses().get(i).getUserDetails());
			addresses.add(address);
		}
		userDto.setAddresses(addresses);
		UserEntity user = modelMapper.map(userDto, UserEntity.class);
		UserEntity updatedUser = userRepository.save(user);
		
		UserDto returnValue = modelMapper.map(updatedUser, UserDto.class);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> listUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> userPage = userRepository.findAll(pageRequest);
		List<UserEntity> users = userPage.getContent();
		for(UserEntity user: users) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			
			UserDto userDto = modelMapper.map(user, UserDto.class);
			returnValue.add(userDto);
		}
		return returnValue;
	}

}
