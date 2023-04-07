package com.jkngil.pos.users.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jkngil.pos.users.data.RoleEntity;
import com.jkngil.pos.users.data.RoleRepository;
import com.jkngil.pos.users.shared.RoleDto;


@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Collection<RoleDto> listRoles() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Collection<RoleDto> roles = new ArrayList<>();
		Collection<RoleEntity> roleEntities = new ArrayList<>();
		
		roleRepository.findAll().forEach(roleEntities::add);
		Type listType = new TypeToken<Collection<RoleDto>>() {}.getType();
		roles = modelMapper.map(roleEntities, listType);
		
		return roles;
	}
}
