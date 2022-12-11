package com.loam.stoody;

import com.loam.stoody.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoodyTests {
@Autowired
VideoService videoService;
	@Test
	void contextLoads() {
	}

}
