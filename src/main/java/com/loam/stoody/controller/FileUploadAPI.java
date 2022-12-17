package com.loam.stoody.controller;

import com.loam.stoody.service.aws.S3BucketDetails;
import com.loam.stoody.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
public class FileUploadAPI {
    private static final String FILE_NAME = "fileName";

    @Autowired
    private S3Service s3Service;

    // Presigned URL
    @GetMapping("/api/uploader/generate_key")
    public ResponseEntity<Object> findByName(HttpServletRequest request, @RequestBody(required = false) Map<String, String> params) {
        final String path = request.getServletPath();
        if (params.containsKey(FILE_NAME))
            return new ResponseEntity<>(s3Service.findByName(params.get(FILE_NAME)), HttpStatus.OK);
        return null;
    }

    @PostMapping("/api/uploader/image/save_file")
    public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
        return new ResponseEntity<>(s3Service.save(extension), HttpStatus.OK);
    }
    // Presigned URL End

    // Put Object Upload
    @PostMapping("/api/upload/image/5mb")
    public String putObjectToBucket(@RequestParam("file") MultipartFile multipartFile){
        String _return = null;
        try {
            _return = s3Service.putObject(S3BucketDetails.S3BucketNameStoodyStandardImages,multipartFile);
        } catch (InterruptedException | IOException e) {
            return "Error";
        }

        return _return;
    }

}
