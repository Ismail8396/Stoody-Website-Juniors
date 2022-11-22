package com.loam.stoody.service.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String uploadFile(MultipartFile multipartFile);
}
