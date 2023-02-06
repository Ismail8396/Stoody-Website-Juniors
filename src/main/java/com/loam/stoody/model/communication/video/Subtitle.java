package com.loam.stoody.model.communication.video;

import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.communication.video.Video;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "subtitle")
public class Subtitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language_code")
    private String languageCode;

    @ManyToOne(targetEntity = UserFile.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_file_id", referencedColumnName = "id")
    private UserFile subtitleFile;// TODO: @alimkhowaja Switch to String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;
}