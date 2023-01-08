package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.statistics.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {
}
