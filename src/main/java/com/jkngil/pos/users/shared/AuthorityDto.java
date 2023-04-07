package com.jkngil.pos.users.shared;

import java.io.Serializable;

public class AuthorityDto implements Serializable {

	private static final long serialVersionUID = -2332022138540210906L;
	private long id;
	private String name;

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

}
