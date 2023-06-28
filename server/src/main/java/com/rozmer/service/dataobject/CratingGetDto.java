package com.rozmer.service.dataobject;

import com.rozmer.service.response.PostUserResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CratingGetDto {
	private Integer id;
	private Integer rating;
	private PostUserResponse user;

	@DateTimeFormat(pattern = "DD-MM-YYYY")
	private Date addedDate;

}
