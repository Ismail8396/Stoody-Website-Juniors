package com.loam.stoody.repository;

import com.loam.stoody.model.LanguageModel;
import com.loam.stoody.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);

    @Query(value = "SELECT * FROM USERS u WHERE u.username LIKE %:filter%", nativeQuery = true)
    List<User> findUsernameBySearchKey(@Param("filter") String filter);
}
