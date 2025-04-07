//package com.jeewaloka.digital.jeewalokadigital.mapper;
//
//import com.jeewaloka.digital.jeewalokadigital.dto.Response.UserCredResposeDTO;
//import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
//import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
//import com.jeewaloka.digital.jeewalokadigital.entity.User;
//import com.jeewaloka.digital.jeewalokadigital.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserCredMapper {
//    @Autowired
//    private UserRepository userRepository;
//    public UserCredResposeDTO toDTO(UserCredentials userCredentials){
//        UserCredResposeDTO userCredResposeDTO = new UserCredResposeDTO();
//        userCredResposeDTO.setUserCredID(userCredentials.getUserCredID());
//        userCredResposeDTO.setPassword(userCredentials.getPassword());
//        userCredResposeDTO.setUsername(userCredentials.getUsername());
//        userCredResposeDTO.setUID(userCredentials.getUser().getUID());
//        return userCredResposeDTO;
//    }
//
//    public UserCredentials toEntity(UserCredentialsRequestDTO userCredentialsDTO){
//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setUsername(userCredentialsDTO.getUsername());
//        userCredentials.setPassword(userCredentialsDTO.getPassword());
//        System.out.println(userCredentialsDTO.getUser());
//        User user = userRepository.findById(userCredentialsDTO.getUser())
//                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + userCredentialsDTO.getUser()));
//        userCredentials.setUser(user);
//        return userCredentials;
//    }
//}
