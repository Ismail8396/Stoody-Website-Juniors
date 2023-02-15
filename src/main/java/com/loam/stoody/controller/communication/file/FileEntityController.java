package com.loam.stoody.controller.communication.file;

import com.loam.stoody.dto.api.request.course.SubtitleRequestDTO;
import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.communication.file.UserFileService;
import com.loam.stoody.service.communication.video.VideoService;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(PRL.apiPrefix)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FileEntityController {
    private final UserFileService userFileService;
    private final VideoService videoService;

    // Any other file than video (.mp4)
    @PostMapping("/upload/user/file")
    @CrossOrigin(origins = "http://localhost:3000")// TODO: Configure CORS!
    @ResponseBody
    public OutdoorResponse<?> putObjectToUserFileBucket(@RequestParam("file") MultipartFile multipartFile){
        return userFileService.uploadObject(multipartFile);
    }

    // -> Video File (only .mp4)

    @PostMapping("/upload/user/file/video")
    @CrossOrigin(origins = "http://localhost:3000")// TODO: Configure CORS!
    @ResponseBody
    public OutdoorResponse<?> putVideoObjectToUserFileBucket(@RequestParam("file") MultipartFile multipartFile,
                                                             @RequestParam("videoDuration")Double videoDuration){
        return videoService.uploadVideo(multipartFile, videoDuration);
    }
}
