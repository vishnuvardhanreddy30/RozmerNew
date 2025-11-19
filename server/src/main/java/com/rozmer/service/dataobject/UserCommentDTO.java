package com.rozmer.service.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class UserCommentDTO {
    private Integer commentId;
    private String content;
    private Integer postId;
    private Date addedDate;

    public UserCommentDTO(Integer commentId, String content, Integer postId, Date addedDate) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.addedDate = addedDate;
    }
}