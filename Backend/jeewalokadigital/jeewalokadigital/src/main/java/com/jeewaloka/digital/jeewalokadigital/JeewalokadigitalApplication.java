package com.jeewaloka.digital.jeewalokadigital;

import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JeewalokadigitalApplication implements CommandLineRunner {

	private UserCredRepository userCredRepository;
	private UserRepository userRepository;

	public JeewalokadigitalApplication(UserCredRepository userCredRepository,UserRepository userRepository) {
		this.userCredRepository = userCredRepository;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(JeewalokadigitalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userCredRepository.save(new UserCredentials("user","$2a$10$1ChW3sMMfTd6Opek25PPYe6SEqM2UpziKNQVy67qljNgvZY5rkxnS"));
		userCredRepository.save(new UserCredentials("admin","$2a$10$B.5TK2ttMNcDK0ZPz9ZZDOPn.Tbp.fYb4w3Evwbag9wiciIAFKrWG"));
	}

//	@Bean
//	public ModelMapper modelMapper(){
//		return new ModelMapper();
//	}

}
