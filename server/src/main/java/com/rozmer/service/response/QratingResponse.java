package com.rozmer.service.response;

import java.util.List;
import java.util.Map;

import com.rozmer.service.dataobject.QratingDto;

import com.rozmer.service.entities.Qrating;
import lombok.Data;

@Data
public class QratingResponse {
    private List<QratingDto> qrating;
    private List<Object[]> qAverageRatings;
}
