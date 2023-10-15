package com.jkngil.pos.users.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jkngil.pos.users.models.AddressDetailsModel;
import com.jkngil.pos.users.models.UserAlbumsResponseModel;
import com.jkngil.pos.users.models.UserRequestModel;
import com.jkngil.pos.users.models.UserResponseModel;
import com.jkngil.pos.users.services.AddressService;
import com.jkngil.pos.users.services.UserService;
import com.jkngil.pos.users.shared.AddressDto;
import com.jkngil.pos.users.shared.UserDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
//@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	Environment env;
	
	@Autowired
	AddressService addressService;

	@GetMapping(value="/status", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public String getStatus() {
		return "Users microservice is up" + ", with token = " + env.getProperty("token.secret");
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody UserRequestModel userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		
		UserResponseModel returnValue = modelMapper.map(createdUser, UserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

	@GetMapping(value = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = userService.getUser(userId);
		
		UserResponseModel returnValue = modelMapper.map(userDto, UserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@PutMapping(value = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserResponseModel>  updateUser(@PathVariable String userId, @RequestBody UserRequestModel userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto updatedUser = userService.updateUser(userId, userDto);
		
		UserResponseModel returnValue = modelMapper.map(updatedUser, UserResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

	@DeleteMapping(value = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public HttpStatus deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		return HttpStatus.OK;
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserResponseModel>> listUsers(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="25") int limit) {
		List<UserResponseModel> returnValue = new ArrayList<>();
		List<UserDto> users = userService.listUsers(page, limit);
		
		for(UserDto user: users) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			
			UserResponseModel userDetails = modelMapper.map(user, UserResponseModel.class);
			returnValue.add(userDetails);
		}
		
		userService.listUsers(page, limit);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@GetMapping(value="/{userId}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<AddressDetailsModel>> getUserAddresses(@PathVariable String userId) {
		List<AddressDetailsModel> returnValue = new ArrayList<>();
		List<AddressDto> addressesDto = addressService.getUserAddresses(userId);
		
		if(addressesDto != null & !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressDetailsModel>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@GetMapping(value="/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<AddressDetailsModel> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		ModelMapper modelMapper = new ModelMapper();
		AddressDto addressesDto = addressService.getUserAddress(userId, addressId);
		AddressDetailsModel returnValue = modelMapper.map(addressesDto, AddressDetailsModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
//	microservices communication test
	@GetMapping(value="/{userId}/albums", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasAuthority('READ')")
//	@PreAuthorize("hasRole('USER') or principal == #userId")
//	@PostAuthorize("principal == returnObject.getBody().getUserId()")
	public ResponseEntity<UserAlbumsResponseModel> getUserDetailsWithAlbums(@PathVariable("userId") String userId) {
		
		UserDto userDto = userService.getUserAlbums(userId);
		UserAlbumsResponseModel returnValue = new ModelMapper().map(userDto, UserAlbumsResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
}
