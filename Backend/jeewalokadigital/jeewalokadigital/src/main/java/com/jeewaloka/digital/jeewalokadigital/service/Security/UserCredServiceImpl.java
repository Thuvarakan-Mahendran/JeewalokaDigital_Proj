package com.jeewaloka.digital.jeewalokadigital.service.Security;

import com.jeewaloka.digital.jeewalokadigital.dto.UserCredentialsDTO;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredServiceImpl implements UserCredService {
    @Autowired
    private UserCredRepository userCredRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserCredentials findById(Long id){
        Optional<UserCredentials> user = userCredRepository.findById(id);
        UserCredentials userCredentials = user.get();
        return userCredentials;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userCredRepository.existsByUsername(username);
    }

    @Override
    public void addUserCredentials(UserCredentialsDTO userCredentialsDTO) {
        if(existsByUsername(userCredentialsDTO.getUsername())){
            throw new IllegalArgumentException("username already exists");
        }
        String encrptedpassword = passwordEncoder.encode(userCredentialsDTO.getPassword());
        userCredentialsDTO.setPassword(encrptedpassword);
        System.out.println(userCredentialsDTO);
        userCredRepository.save(modelMapper.map(userCredentialsDTO,UserCredentials.class));
    }
}
