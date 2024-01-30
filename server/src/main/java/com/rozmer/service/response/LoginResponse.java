package com.rozmer.service.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rozmer.service.dataobject.UserFollower;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class LoginResponse {

	@JsonIgnore
	private ModelMapper modelMapper = new ModelMapper();

	@JsonIgnore
	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	private Long userId;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String mobileNo;

	private String username;

	private String role;

	private boolean loggedIn;

	public void setFollowers(List<com.rozmer.service.entities.UserFollower> entityList) {
		Type listType = new TypeToken<List<UserFollower>>() {}.getType();

		this.followers = modelMapper.map(entityList, listType);
	}

	public void setFollowings(List<com.rozmer.service.entities.UserFollower> entityList) {
		Type listType = new TypeToken<List<UserFollower>>() {}.getType();

		this.followings = modelMapper.map(entityList, listType);
	}

	private List<UserFollower> followers = new ArrayList<>();

	private List<UserFollower> followings = new ArrayList<>();

	public List<UserFollower> mapUserFollowerEntitiesToDTOs(List<com.rozmer.service.entities.UserFollower> entityList) {
		Type listType = new TypeToken<List<UserFollower>>() {}.getType();
		return modelMapper.map(entityList, listType);
	}


	
}
