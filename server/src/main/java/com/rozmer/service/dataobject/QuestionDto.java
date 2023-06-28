package com.rozmer.service.dataobject;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.rozmer.service.response.PostUserResponse;
import com.rozmer.service.response.QratingResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private String questions;
    private Long questionId;
    private List<AnswerDto> answer;
    private PostUserResponse user;

    @DateTimeFormat(pattern = "DD-MM-YYYY")
    private Date addedDate;

    private QratingResponse qrating;
}
