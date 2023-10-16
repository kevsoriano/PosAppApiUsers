package com.jkngil.pos.users.services;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.catalina.Role;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jkngil.pos.users.data.AlbumsServiceClient;
import com.jkngil.pos.users.data.AuthorityEntity;
import com.jkngil.pos.users.data.RoleEntity;
import com.jkngil.pos.users.data.RoleRepository;
import com.jkngil.pos.users.data.UserEntity;
import com.jkngil.pos.users.data.UserRepository;
import com.jkngil.pos.users.models.AlbumResponseModel;
import com.jkngil.pos.users.shared.AddressDto;
import com.jkngil.pos.users.shared.RoleDto;
import com.jkngil.pos.users.shared.UserDto;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
//	RestTemplate restTemplate;
	AlbumsServiceClient albumsServiceClient;
	RoleRepository roleRepository;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserServiceImpl(
			UserRepository userRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder,
//			RestTemplate restTemplate
			AlbumsServiceClient albumsServiceClient,
			RoleRepository roleRepository
			) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//		this.restTemplate = restTemplate;
		this.albumsServiceClient = albumsServiceClient;
		this.roleRepository = roleRepository;
	}

//	@Override
//	public UserDto createUser(UserDto userDetails) {
//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//		//	assign IDs to addresses
//		for(int i = 0; i<userDetails.getAddresses().size();i++) {
//			AddressDto address = userDetails.getAddresses().get(i);
//			address.setUserDetails(userDetails);
//			address.setAddressId(UUID.randomUUID().toString());
//			userDetails.getAddresses().set(i, address);
//		}
//
//		Collection<RoleDto> roles = new ArrayList<>();
//		for(Iterator<RoleDto> iterator = userDetails.getRoles().iterator(); iterator.hasNext();) {
//			RoleDto role = iterator.next();
//			RoleEntity roleEntity = roleRepository.findByName(role.getName());
//			if(roleEntity != null) {
//				roles.add(modelMapper.map(roleEntity, RoleDto.class));
//			}
//		}
//
//		userDetails.setRoles(roles);
//		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
//		userEntity.setUserId(UUID.randomUUID().toString());
//		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
//		UserEntity savedUser = userRepository.save(userEntity);
//
//		UserDto returnValue = modelMapper.map(savedUser, UserDto.class);
//		return returnValue;
//	}


	public UserEntity createUser(UserEntity userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		//TODO - fix saving of user's addresses

		//	assign IDs to addresses
//		for(int i = 0; i<userDetails.getAddresses().size();i++) {
//			AddressDto address = userDetails.getAddresses().get(i);
//			address.setUserDetails(userDetails);
//			address.setAddressId(UUID.randomUUID().toString());
//			userDetails.getAddresses().set(i, address);
//		}


		if (CollectionUtils.isNotEmpty(userDetails.getRoles())) {
			List<String> roleNames = userDetails.getRoles().stream()
							.map(RoleEntity::getName)
							.toList();
			userDetails.setRoles(roleRepository.findByNameIn(roleNames));
		}
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setUserId(UUID.randomUUID().toString());
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		return userRepository.save(userEntity);
	}

	@Override
	public UserDto getUser(String userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = userRepository.findByUserId(userId);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = userRepository.findByEmail(email);

		if(userEntity == null) throw new UsernameNotFoundException(email);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDetails) {
		List<AddressDto> addresses = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = userRepository.findByUserId(userId);

		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		userDto.setFirstName(userDetails.getFirstName());
		userDto.setLastName(userDetails.getLastName());
		for(int i = 0; i<userDetails.getAddresses().size();i++) {
			AddressDto address = userDetails.getAddresses().get(i);
			address.setAddressId(userEntity.getAddresses().get(i).getAddressId());
			address.setId(userEntity.getAddresses().get(i).getId());
			address.setUserDetails(userDto.getAddresses().get(i).getUserDetails());
			addresses.add(address);
		}
		userDto.setAddresses(addresses);

		UserEntity user = modelMapper.map(userDto, UserEntity.class);
		UserEntity updatedUser = userRepository.save(user);

		UserDto returnValue = modelMapper.map(updatedUser, UserDto.class);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> listUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> userPage = userRepository.findAll(pageRequest);
		List<UserEntity> users = userPage.getContent();
		for(UserEntity user: users) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			UserDto userDto = modelMapper.map(user, UserDto.class);
			returnValue.add(userDto);
		}
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if(userEntity == null) throw new UsernameNotFoundException(username);

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Collection<RoleEntity> roles = userEntity.getRoles();

		roles.forEach((role) -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));

			Collection<AuthorityEntity> entities = role.getAuthorities();
			entities.forEach((authority) -> {
				authorities.add(new SimpleGrantedAuthority(authority.getName()));
			});
		});

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
				true, true, true, true,
				authorities);
	}

//	microservices communication test
	@Override
	public UserDto getUserAlbums(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null) throw new UsernameNotFoundException("User not found");
		UserDto returnValue = new ModelMapper().map(userEntity, UserDto.class);

		String albumsUrl = "http://ALBUMS-WS/users/12312312/albums";

//		RestTemplate
//		ResponseEntity<List<AlbumResponseModel>> albumListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
//		List<AlbumResponseModel> albumList = albumListResponse.getBody();

//		Handle FeignException with try/catch block
//		List<AlbumResponseModel> albumList = null;
//		try {
//			albumList = albumsServiceClient.getAlbums(userId);
//		} catch (FeignException e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getLocalizedMessage());
//		}

//		Handle FeignException with FeignErrorDecoder
		logger.debug("Before calling the albums Microservice");
		List<AlbumResponseModel> albumList = albumsServiceClient.getAlbums(userId);
		logger.debug("After calling the albums Microservice");
		returnValue.setAlbums(albumList);

		return returnValue;
	}

}
