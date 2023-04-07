package com.jkngil.pos.users.models;

import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequestModel {
	@NotNull(message="First name cannot be null")
	private String firstName;
	@NotNull(message="Last name cannot be null")
	private String lastName;
	@NotNull(message="Email cannot be null")
	@Email
	private String email;
	@NotNull(message="Password cannot be null")
	@Size(min=8, max=16, message="Password must be greater than or equal to 8 characters and lower than or equal to 16")
	private String password;
	@NotNull
	private List<AddressDetailsModel> addresses;
	@NotNull
	private Collection<RoleDetailsModel> roles;

	public Collection<RoleDetailsModel> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleDetailsModel> roles) {
		this.roles = roles;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AddressDetailsModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDetailsModel> addresses) {
		this.addresses = addresses;
	}

}
