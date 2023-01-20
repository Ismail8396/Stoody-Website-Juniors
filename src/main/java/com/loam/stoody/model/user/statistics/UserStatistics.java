package com.loam.stoody.model.user.statistics;

import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private UserLevel userLevel = UserLevel.Beginner;
    private Boolean verified = false;

    public String getUserLevelString(){
        return "global."+userLevel.toString().toLowerCase();
    }
    public Integer getUserLevelNumber(){
        return userLevel.ordinal();
    }
    public String getRoleDisplayLabel(){
        return "Instructor";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserStatistics that = (UserStatistics) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
