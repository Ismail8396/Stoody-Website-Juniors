package com.loam.stoody.global;

import com.loam.stoody.model.ParentModel;

import java.time.LocalDateTime;

public class Utility {

    public void setInsertProperties(ParentModel parentModel) {
        parentModel.setCreatedDate(LocalDateTime.now());
        parentModel.setCreatedBy(1l);//@todo dynamic
    }

    public void setUpdateProperties(ParentModel parentModel) {
        parentModel.setModifiedDate(LocalDateTime.now());
        parentModel.setCreatedBy(1l);//@todo dynamic
    }
}
