package com.jeewaloka.digital.jeewalokadigital;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JeewalokadigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeewalokadigitalApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
