package com.jeewaloka.digital.jeewalokadigital.service.Security;

import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserCredResposeDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.mapper.UserCredMapper;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCredServiceImpl implements UserCredService {
    @Autowired
    private UserCredRepository userCredRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserCredMapper userCredMapper;
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
    public void addUserCredentials(UserCredentialsRequestDTO userCredentialsDTO) {
        if(existsByUsername(userCredentialsDTO.getUsername())){
            throw new IllegalArgumentException("username already exists");
        }
        String encrptedpassword = passwordEncoder.encode(userCredentialsDTO.getPassword());
        userCredentialsDTO.setPassword(encrptedpassword);
        System.out.println(userCredentialsDTO);
        UserCredentials userCred = userCredMapper.toEntity(userCredentialsDTO);
        System.out.println(userCred.getUsername());
        userCredRepository.save(userCred);
    }

    @Override
    public List<UserCredResposeDTO> getUserCred() {
        return userCredRepository.findAll().stream()
                .map(userCredMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteUserCred(Long id) {
        userCredRepository.deleteById(id);
    }

    @Override
    public Long findUserID(String username) {
        return userCredRepository.findByUsername(username).get().getUser().getUID();
    }
}
