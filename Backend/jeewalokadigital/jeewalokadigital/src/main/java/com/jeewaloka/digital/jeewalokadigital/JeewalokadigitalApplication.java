package com.jeewaloka.digital.jeewalokadigital;

import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JeewalokadigitalApplication{
	public static void main(String[] args) {
		SpringApplication.run(JeewalokadigitalApplication.class, args);
	}
}
