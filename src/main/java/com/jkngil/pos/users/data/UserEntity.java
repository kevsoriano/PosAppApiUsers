package com.jkngil.pos.users.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jkngil.pos.users.shared.UserDto;
import jakarta.persistence.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity(name = "users")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = -4023412725306339832L;
	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false, unique = true)
	private String userId;
	@Column(length = 50, nullable = false)
	private String firstName;
	@Column(length = 50, nullable = false)
	private String lastName;
	@Column(length = 120, nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String encryptedPassword;
	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<AddressEntity> addresses;
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
	private List<RoleEntity> roles;

	@Transient
	private String password;

	public UserEntity() {}
	public UserEntity(UserDto dto) {
		id = dto.getId();
		userId = dto.getUserId();
		firstName = dto.getFirstName();
		lastName = dto.getLastName();
		email = dto.getEmail();
		encryptedPassword = dto.getEncryptedPassword();
		//TODO - add setting of addresses
		if (CollectionUtils.isNotEmpty(dto.getRoles())) {
			List<RoleEntity> roles = new ArrayList<>();
			dto.getRoles().forEach(r -> roles.add(new RoleEntity(r)));
			this.roles = roles;
		}

		password = dto.getPassword();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
