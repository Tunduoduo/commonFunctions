package com.example.functions;

import com.example.functions.service.CrawlingService;
import com.example.functions.service.SendEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FunctionsApplicationTests {
	@Autowired
	SendEmailService sendEmailService;
	@Autowired
	CrawlingService crawlingService;

	@Test
	void contextLoads() {
	}
	@Test
	void sendEmail(){
		sendEmailService.sendSimpleMessage("fiona.tiann@gmail.com", "Test", "test");
	}
	@Test
	void crawlingPost(){
		crawlingService.getPost();
	}


}
