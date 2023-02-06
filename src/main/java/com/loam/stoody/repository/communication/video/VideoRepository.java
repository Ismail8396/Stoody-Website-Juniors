package com.loam.stoody.repository.communication.video;

import com.loam.stoody.model.communication.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}

