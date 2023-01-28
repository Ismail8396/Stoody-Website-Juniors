package com.loam.stoody.model.communication.notification;

import com.loam.stoody.enums.SimpleNotificationBadge;
import com.loam.stoody.model.ParentModel;
import com.loam.stoody.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="simple_notifications")
public class SimpleNotification extends ParentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sender_fk")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_fk")
    private User receiver;

    private String content;
    private SimpleNotificationBadge badge = SimpleNotificationBadge.SNB_INFO;
    private boolean read = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SimpleNotification that = (SimpleNotification) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
