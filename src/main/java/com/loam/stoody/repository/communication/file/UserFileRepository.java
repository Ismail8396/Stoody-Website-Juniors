package com.loam.stoody.repository.communication.file;

import com.loam.stoody.model.communication.file.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
}
