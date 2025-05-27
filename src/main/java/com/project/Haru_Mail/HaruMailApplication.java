package com.project.Haru_Mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HaruMailApplication {
	public static void main(String[] args) {
		SpringApplication.run(HaruMailApplication.class, args);
	}

}
