package com.rozmer.service.dataobject;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.rozmer.service.response.CratingResponse;
import com.rozmer.service.response.PostUserResponse;

import lombok.Data;

@Data
public class CommentDto {
	
	private Integer id;
	private String content;
	private PostUserResponse user;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;	

	private CratingResponse crating;

}
