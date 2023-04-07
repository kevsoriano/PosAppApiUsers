package com.jkngil.pos.users.services;

import java.util.Collection;

import com.jkngil.pos.users.shared.AuthorityDto;

public interface AuthorityService {
	Collection<AuthorityDto> listAuthorities();
}
