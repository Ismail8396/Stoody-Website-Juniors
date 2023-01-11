package com.loam.stoody.repository.communication.report;

import com.loam.stoody.model.communication.report.ReportedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedUserRepository extends JpaRepository<ReportedUser, Long> {
}
