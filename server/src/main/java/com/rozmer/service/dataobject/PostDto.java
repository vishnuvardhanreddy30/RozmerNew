package com.rozmer.service.dataobject;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.rozmer.service.response.PostUserResponse;
import com.rozmer.service.response.PratingResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {

	private Integer postId;

	private String title;

	private String content;

	private String imageName;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

	private PostUserResponse user;

	private PratingResponse prating;

	private String hidePost;
	//private Set<Prating> prating;
	//private List<Set<Prating>> prating;

	// private Set<CommentDto> comments=new HashSet<>();

}
