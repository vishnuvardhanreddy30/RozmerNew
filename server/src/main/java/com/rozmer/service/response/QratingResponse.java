package com.rozmer.service.response;

import java.util.List;
import java.util.Map;

import com.rozmer.service.dataobject.QratingDto;

import lombok.Data;

@Data
public class QratingResponse {
    private List<QratingDto> qrating;
    private Map<String, String> qAverageRatings;
}
