package com.rozmer.service.dataobject;

public class UserCommentDTO {
    private Integer commentId;
    private String content;
    private Integer postId;

    public UserCommentDTO(Integer commentId, String content, Integer postId) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
    }
}