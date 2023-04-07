package com.jkngil.pos.users.data;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity(name = "authorities")
public class AuthorityEntity implements Serializable {
	
	private static final long serialVersionUID = -806528954184212417L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false, length = 20, unique = true)
	private String name;
	@ManyToMany(mappedBy = "authorities")
	private Collection<RoleEntity> roles;

	public AuthorityEntity() {}

	public AuthorityEntity(String name) {
		this.name=name;
	}
	
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

	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEntity> roles) {
		this.roles = roles;
	}

}
