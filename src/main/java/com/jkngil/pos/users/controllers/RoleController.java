package com.jkngil.pos.users.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
//@CrossOrigin
public class RoleController {
	@Autowired
	RoleService roleService;
	
	@GetMapping
	public ResponseEntity<List<RoleDetailsModel>> listRoles() {
		ModelMapper modelMapper = new ModelMapper();
		List<RoleDetailsModel> returnValue = new ArrayList<>();
		List<RoleDto> roles = roleService.listRoles();
		
		Type listType = new TypeToken<List<RoleDetailsModel>>() {}.getType();
		returnValue = modelMapper.map(roles, listType);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
