package com.loam.stoody.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListAPIResponse<T> {
    int recordCount;
    int visibleRecordCount;
    int pageCount;
    T response;
}
