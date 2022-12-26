package com.loam.stoody.repository.user;

import com.loam.stoody.model.user.requests.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long> {

    public LoginRequest findLoginRequestByUsername(String username);
}
