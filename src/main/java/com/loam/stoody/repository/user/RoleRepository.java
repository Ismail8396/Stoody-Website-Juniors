package com.loam.stoody.repository.user;

import com.loam.stoody.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM roles r WHERE r.name LIKE %:filter%", nativeQuery = true)
    List<Role> findBySearchKey(@Param("filter") String filter);
}
