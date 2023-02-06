package com.loam.stoody.model.communication.file;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = {
        @Index(name = "userFileIndex_id", columnList = "id", unique = true)
})
public class UserFile extends FileEntity {
}
