package com.jkngil.pos.users.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jkngil.pos.users.models.AuthorityDetailsModel;
import com.jkngil.pos.users.services.AuthorityService;
import com.jkngil.pos.users.shared.AuthorityDto;

@RestController
@RequestMapping("/authorities")
//@CrossOrigin
public class AuthorityController {
	@Autowired
	AuthorityService authorityService;
	
	@GetMapping
	public ResponseEntity<Collection<AuthorityDetailsModel>> listAuthorities() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Collection<AuthorityDetailsModel> returnValue = new ArrayList<>();
		Collection<AuthorityDto> authorities = authorityService.listAuthorities();
		
		Type listType = new TypeToken<Collection<AuthorityDetailsModel>>() {}.getType();
		returnValue = modelMapper.map(authorities, listType);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
