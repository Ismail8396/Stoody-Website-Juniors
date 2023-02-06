package com.loam.stoody.controller.communication.file;

import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.communication.file.UserFileService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(PRL.apiPrefix)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserFileController {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserFileService userFileService;

    // Put Object Upload
    @PostMapping("/upload/user/file")
    @CrossOrigin(origins = "http://localhost:3000")// TODO: Configure CORS!
    @ResponseBody
    public OutdoorResponse<?> putObjectToUserFileBucket(@RequestParam("file") MultipartFile multipartFile){
        return userFileService.uploadObject(multipartFile);
    }
}
