package com.jeewaloka.digital.jeewalokadigital;

import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JeewalokadigitalApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCredRepository userCredRepository;
	public static void main(String[] args) {
		SpringApplication.run(JeewalokadigitalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(userRepository.findAll().isEmpty()) {
			UserCredentials userCredentials = new UserCredentials("swag", "$2a$10$vK/SXjkWxf/xEqMNO5qq2OPlmyWXBHzPhr3PeUw4ROnhMaz8riFfy", UserRole.ADMIN);
			userCredRepository.save(userCredentials);
			User user = new User("swag", "0192837465", "swag@gmail.com", userCredentials);
			userRepository.save(user);
			userCredentials.setUser(user);
			userCredRepository.save(userCredentials);

			UserCredentials userCredentials2 = new UserCredentials("pot", "$2a$10$ZtNU7397SuaZ6MWWnxsB5.Uz.4hYvIbrsvLjh.ZMpc8Fm58Ss0QCm", UserRole.ADMIN);
			userCredRepository.save(userCredentials2);
			User user2 = new User("pot", "0192037465", "pot@gmail.com", userCredentials2);
			userRepository.save(user2);
			userCredentials2.setUser(user2);
			userCredRepository.save(userCredentials2);
		}
	}
}
