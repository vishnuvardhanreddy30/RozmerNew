package com.rozmer.service.dataobject;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.rozmer.service.response.PostUserResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDtos {

	private Integer postId;

	private String title;

	private String imageName;
	
	private String hidePost;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

	private PostUserResponse user;

	private Set<PratingGetDto> prating;

	// private Set<CommentDto> comments=new HashSet<>();

}
