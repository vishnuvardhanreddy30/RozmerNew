package com.rozmer.service.dataobject;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.rozmer.service.response.PostUserResponse;

import lombok.Data;

@Data
public class QratingDto {

	private Integer id;
	private Integer rating;
	private PostUserResponse user;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

}
