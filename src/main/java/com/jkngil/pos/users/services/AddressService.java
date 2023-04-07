package com.jkngil.pos.users.services;

import java.util.List;

import com.jkngil.pos.users.shared.AddressDto;

public interface AddressService {
	AddressDto getUserAddress(String userId, String addressId);
	List<AddressDto> getUserAddresses(String userId);
}
