package com.loam.stoody.controller.communication;

import com.loam.stoody.dto.api.request.course.SubtitleRequestDTO;
import com.loam.stoody.dto.api.request.course.VideoRequestDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.communication.video.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(PRL.apiPrefix)
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/user/videos/save/{saveSubtitles}")
    @ResponseBody
    public OutdoorResponse<?> postUserVideo(@PathVariable("saveSubtitles") Boolean saveSubtitles, @RequestBody VideoRequestDTO video) {
        return videoService.saveConverted(video, saveSubtitles);
    }

    @GetMapping("/user/videos/get/all")
    @ResponseBody
    public OutdoorResponse<?> getUserVideos() {
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, videoService.getUserVideos());
    }

    @PostMapping("/user/videos/subtitles/save")
    @ResponseBody
    public OutdoorResponse<?> postUserVideoSubtitle(@RequestBody SubtitleRequestDTO subtitle) {
        return videoService.saveSubtitle(subtitle);
    }


}
