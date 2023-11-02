package com.jkngil.pos.users.models;

import java.util.Collection;
import java.util.List;

public class UserResponseModel {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressDetailsModel> addresses;
	private List<RoleDetailsModel> roles;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AddressDetailsModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDetailsModel> addresses) {
		this.addresses = addresses;
	}

	public List<RoleDetailsModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDetailsModel> roles) {
		this.roles = roles;
	}

}
