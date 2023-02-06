package com.loam.stoody.model.communication.video;

import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="video", indexes = {
        @Index(name = "videoIndex_id", columnList = "id", unique = true)
})
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserFile.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_file_id", referencedColumnName = "id")
    private UserFile userFile;

    private Long videoDuration;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "video")
    private Set<Subtitle> subtitles = new HashSet<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}