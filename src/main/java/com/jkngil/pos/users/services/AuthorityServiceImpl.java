package com.jkngil.pos.users.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jkngil.pos.users.data.AuthorityEntity;
import com.jkngil.pos.users.data.AuthorityRepository;
import com.jkngil.pos.users.shared.AuthorityDto;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	AuthorityRepository authorityRepository;
	
	
	@Override
	public Collection<AuthorityDto> listAuthorities() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Collection<AuthorityDto> returnValue = new ArrayList<>();
		Collection<AuthorityEntity> authorities = new ArrayList<>();
		
		authorityRepository.findAll().forEach(authorities::add);
		Type listType = new TypeToken<Collection<AuthorityDto>>() {}.getType();
		returnValue = modelMapper.map(authorities, listType);
		
		return returnValue;
	}

}
