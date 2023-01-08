package com.loam.stoody.controller.rest_controllers;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.utils.aws.S3BucketDetails;
import com.loam.stoody.service.utils.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
// TODO: Configure CORS!
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadAPI {
    private static final String FILE_NAME = "fileName";

    private final S3Service s3Service;
//
//    // Presigned URL----------------------------------------------------------------------------------------------------
//    @GetMapping("/api/uploader/generate_key")
//    public ResponseEntity<Object> findByName(HttpServletRequest request, @RequestBody(required = false) Map<String, String> params) {
//        final String path = request.getServletPath();
//        if (params.containsKey(FILE_NAME))
//            return new ResponseEntity<>(s3Service.findByName(params.get(FILE_NAME)), HttpStatus.OK);
//        return null;
//    }
//
//    @PostMapping("/api/uploader/image/save_file")
//    public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
//        return new ResponseEntity<>(s3Service.save(extension), HttpStatus.OK);
//    }
//    // Presigned URL End------------------------------------------------------------------------------------------------
//
    // Put Object Upload
    @PostMapping(PRL.apiPrefix+"/upload/put/small")
    // TODO: Configure CORS!
    @CrossOrigin(origins = "http://localhost:3000")
    public OutdoorResponse<?> putObjectToBucket(@RequestParam("file") MultipartFile multipartFile){
        try{
            return new OutdoorResponse<>(IndoorResponse.SUCCESS,
                    s3Service.putObject(S3BucketDetails.S3BucketNameStoodyStandardImages, multipartFile));
        }catch (Exception ignore){}
        return new OutdoorResponse<>(IndoorResponse.FAIL, "FAILED");
    }
}
