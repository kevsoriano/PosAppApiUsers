package com.jkngil.pos.users.models;

import java.util.Collection;

public class RoleDetailsModel {
	private long id;
	private String name;
	private Collection<AuthorityDetailsModel> authorities;

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

	public Collection<AuthorityDetailsModel> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<AuthorityDetailsModel> authorities) {
		this.authorities = authorities;
	}

}
