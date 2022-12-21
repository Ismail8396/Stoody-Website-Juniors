package com.loam.stoody;

import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.loam.stoody.model.user_models.Role;
import com.loam.stoody.model.user_models.User;
import com.loam.stoody.repository.user_repo.RoleRepository;
import com.loam.stoody.repository.user_repo.UserRepository;
import com.loam.stoody.service.communication_service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class StoodyTests {
	@Test
	void contextLoads() {
	}
}
