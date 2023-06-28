package com.rozmer.service.dataobject;

import lombok.Data;

@Data
public class RatingDto {

    private int rating;
    private int type;
    private int parentId;
    
}
