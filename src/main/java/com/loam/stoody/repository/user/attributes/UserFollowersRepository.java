package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.UserFollowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowersRepository extends JpaRepository<UserFollowers, Long> {
}
