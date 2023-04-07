package com.jkngil.pos.users.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jkngil.pos.users.models.RoleDetailsModel;
import com.jkngil.pos.users.services.RoleService;
import com.jkngil.pos.users.shared.RoleDto;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	RoleService roleService;
	
	@GetMapping
	public ResponseEntity<Collection<RoleDetailsModel>> listRoles() {
		ModelMapper modelMapper = new ModelMapper();
		Collection<RoleDetailsModel> returnValue = new ArrayList<>();
		Collection<RoleDto> roles = roleService.listRoles();
		
		Type listType = new TypeToken<Collection<RoleDetailsModel>>() {}.getType();
		returnValue = modelMapper.map(roles, listType);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
