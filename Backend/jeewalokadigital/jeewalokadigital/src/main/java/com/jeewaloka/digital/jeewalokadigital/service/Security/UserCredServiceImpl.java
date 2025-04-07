package com.jeewaloka.digital.jeewalokadigital.service.Security;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserCredentialsResposeDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCredServiceImpl implements UserCredService {
    @Autowired
    private UserCredRepository userCredRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    public UserCredentials findById(Long id){
        Optional<UserCredentials> userCredentials = userCredRepository.findById(id);
        if(userCredentials.isEmpty()){
            throw new RuntimeException("User credentials by the id " + id + " is not found");
        }
        return userCredentials.get();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userCredRepository.existsByUsername(username);
    }

    @Override
    public void addUserCredentials(UserCredentialsRequestDTO userCredentialsRequestDTO) {
        if(existsByUsername(userCredentialsRequestDTO.getUsername())){
            throw new IllegalArgumentException("username already exists");
        }
        String encrptedpassword = passwordEncoder.encode(userCredentialsRequestDTO.getPassword());
        userCredentialsRequestDTO.setPassword(encrptedpassword);
        System.out.println(userCredentialsRequestDTO);
        UserCredentials userCred = modelMapper.map(userCredentialsRequestDTO,UserCredentials.class);
        Optional<User> user = userRepository.findById(userCredentialsRequestDTO.getUser());
        if(user.isEmpty()){
            throw new RuntimeException("User with user id " + userCredentialsRequestDTO.getUser() + "is not found");
        }
        userCred.setUser(user.get());
        System.out.println(userCred.getUsername());
        userCredRepository.save(userCred);
    }

//    @Override
//    public List<UserCredResposeDTO> getUserCred() {
//        return userCredRepository.findAll().stream()
//                .map(userCredMapper::toDTO)
//                .toList();

//    }

    @Override
    public void deleteUserCred(Long id) {
        userCredRepository.deleteById(id);
    }

    @Override
    public Long findUserID(String username) {
        Optional<UserCredentials> userCredentials = userCredRepository.findByUsername(username);
        if(userCredentials.isEmpty()){
            throw new UsernameNotFoundException("There is no user credentials by username " + username);
        }
        return userCredentials.get().getUser().getUID();
    }

    @Override
    public String findRole(String username) {
        Optional<UserCredentials> userCredentials = userCredRepository.findByUsername(username);
        if(userCredentials.isEmpty()){
            throw new NotFoundException("usercred record is not found");
        }
        return userCredentials.get().getRole().name();
    }

    @Override
    public List<UserCredentialsResposeDTO> findAll() {
        return userCredRepository.findAll().stream()
                .map(userCredentials -> {
                    UserCredentialsResposeDTO dto = modelMapper.map(userCredentials,UserCredentialsResposeDTO.class);
                    dto.setUser(userCredentials.getUser().getUID());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
