package com.loam.stoody.repository.communication.video;

import com.loam.stoody.model.communication.video.Subtitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtitlesRepository extends JpaRepository<Subtitle, Long> {
}
