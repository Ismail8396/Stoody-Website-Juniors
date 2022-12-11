package com.loam.stoody.dto;

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
