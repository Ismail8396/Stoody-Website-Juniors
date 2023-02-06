package com.loam.stoody.controller.development;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Deprecated
// TODO: Configure CORS!
public class FileUploadAPI {
    private static final String FILE_NAME = "fileName";


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

}
