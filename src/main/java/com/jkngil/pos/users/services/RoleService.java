package com.jkngil.pos.users.services;

import java.util.Collection;

import com.jkngil.pos.users.shared.RoleDto;

public interface RoleService {
	Collection<RoleDto> listRoles();
}
