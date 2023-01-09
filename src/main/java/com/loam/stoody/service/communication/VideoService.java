package com.loam.stoody.service.communication;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.communication.video.Video;
import com.loam.stoody.repository.communication.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@UnderDevelopment
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository){
        this.videoRepository = videoRepository;
    }

    public void uploadVideo(MultipartFile file){
        //Video video = new Video();
        //video.setVideoUrl(videoURL);

        //videoRepository.save(video);
    }

    public void createTestVideo(){
        Video video = new Video();
        video.setTitle("Hello World");
        video.setDescription("Going to");
        video.setSubtitlesURL("receroty");
        video.setVideoUrl("asdasd");
        video.setThumbnailUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png");
        videoRepository.save(video);
    }

}
