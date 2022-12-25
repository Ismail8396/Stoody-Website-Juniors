package com.loam.stoody.repository.user;

import com.loam.stoody.model.user.requests.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingRegistrationRequests extends JpaRepository<RegistrationRequest, Integer> {
}
