package com.loam.stoody.dto.api.response;

import com.loam.stoody.global.constants.IndoorResponse;
import lombok.Data;
import lombok.Getter;

@Data
public class OutdoorResponse<T> {
    String responseStatus;
    T response;
    public OutdoorResponse(IndoorResponse indoorResponse, T response){
        this.responseStatus = indoorResponse.toString();
        this.response = response;
    }
}
