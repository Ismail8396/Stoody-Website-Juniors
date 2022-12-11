package com.loam.stoody.service;

import com.loam.stoody.model.Video;
//import com.loam.stoody.repository.VideoRepository;
import com.loam.stoody.repository.VideoRepository;
import com.loam.stoody.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {
    @Autowired
    private final S3Service s3Service;
    @Autowired
    private final VideoRepository videoRepository;

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
        video.setCourse(null);

        videoRepository.save(video);
    }

}
