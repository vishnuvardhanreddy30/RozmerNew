package com.rozmer.service.response;

import java.util.List;
import java.util.Map;

import com.rozmer.service.dataobject.PratingGetDto;

import com.rozmer.service.entities.AverageRating;
import com.rozmer.service.entities.Prating;
import lombok.Data;

@Data
public class PratingResponse {
    private List<PratingGetDto> pratingGetDto;
    private List<Object[]> pAverageRating;
}
