package com.rozmer.service.response;

import java.util.List;

import com.rozmer.service.dataobject.QratingDto;

import lombok.Data;

@Data
public class QratingResponse {
    private List<QratingDto> qrating;
}
