package com.jkngil.pos.users.shared;

import java.io.Serializable;
import java.util.Collection;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = -2697989606426285138L;
	private long id;
	private String name;
	private Collection<AuthorityDto> authorities;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<AuthorityDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

}
