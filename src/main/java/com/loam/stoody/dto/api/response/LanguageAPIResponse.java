/*
@fileName:  LanguageAPIResponse

@aka:       Language Application Interface Response

@purpose:   Use this to as the return type of rest-controller of any Language API.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageAPIResponse<T> {
    int recordCount;
    T response;
}
