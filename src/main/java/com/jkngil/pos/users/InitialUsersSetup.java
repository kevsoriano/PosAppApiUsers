package com.jkngil.pos.users;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jkngil.pos.users.data.AuthorityEntity;
import com.jkngil.pos.users.data.AuthorityRepository;
import com.jkngil.pos.users.data.RoleEntity;
import com.jkngil.pos.users.data.RoleRepository;
import com.jkngil.pos.users.data.UserEntity;
import com.jkngil.pos.users.data.UserRepository;

//@Component
public class InitialUsersSetup {

	@Autowired
	AuthorityRepository authorityRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
//	@Autowired
//	BCryptPasswordEnccoder bCryptPasswordEnccoder;
	
	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("From application ready event...");
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		RoleEntity roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
		RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
	
		if(roleAdmin == null) return;
		
		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("Admin");
		adminUser.setEmail("admin@admin.com");
		adminUser.setUserId(UUID.randomUUID().toString());
		adminUser.setEncryptedPassword("12345678");
		adminUser.setRoles(Arrays.asList(roleAdmin));
		
		if(userRepository.findByEmail(adminUser.getEmail()) == null) userRepository.save(adminUser);
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		AuthorityEntity authority = authorityRepository.findByName(name);
		if(authority==null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		return authority;
	}
	
	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		RoleEntity role = roleRepository.findByName(name);
		if(role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		return role;
	}
}
