package com.jkngil.pos.users.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jkngil.pos.users.data.AddressEntity;
import com.jkngil.pos.users.data.AddressRepository;
import com.jkngil.pos.users.data.UserEntity;
import com.jkngil.pos.users.data.UserRepository;
import com.jkngil.pos.users.shared.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AddressRepository addressRepository;

	@Override
	public AddressDto getUserAddress(String userId, String addressId) {
		AddressDto returnValue = null;
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if(addressEntity!= null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		return returnValue;
	}

	@Override
	public List<AddressDto> getUserAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity address: addresses) {
			returnValue.add(modelMapper.map(address, AddressDto.class));
		}
		return returnValue;
	}

}
