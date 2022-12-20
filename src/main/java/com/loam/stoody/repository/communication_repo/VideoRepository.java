package com.loam.stoody.repository.communication_repo;

import com.loam.stoody.global.annotations.UnderDevelopment;
import com.loam.stoody.model.communication_models.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;

@UnderDevelopment
public interface VideoRepository extends JpaRepository<Video,Integer> {

}
