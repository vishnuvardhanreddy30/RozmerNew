package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.PratingGetDto;

import lombok.Data;

@Data
public class PratingResponse {
    private List<PratingGetDto> pratingGetDto;
}
