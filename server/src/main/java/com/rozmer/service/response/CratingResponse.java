package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.CratingGetDto;

import lombok.Data;

@Data
public class CratingResponse {
    private List<CratingGetDto> crating;
}
