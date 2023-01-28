package com.loam.stoody.dto.api.request;

import lombok.Data;

@Data
public class SearchFilterDTO {
    private String searchKey;
    private String sortFilter;
}
