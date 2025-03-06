package com.jeewaloka.digital.jeewalokadigital.service.Security;

import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;

import com.jeewaloka.digital.jeewalokadigital.repository.UserCredRepository;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserCredRepository userCredRepository;
    public UserDetailsServiceImpl(UserCredRepository userRepository) {
        this.userCredRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("inside userdetailsservice" + username);
        Optional<UserCredentials> user = userCredRepository.findByUsername(username);
        System.out.println("after retrieve user");
        UserBuilder builder = null;
        System.out.println("builder got created");
        if(user.isPresent()){
            System.out.println("user is present");
            UserCredentials currentUser = user.get();
//            System.out.println(currentUser.getUsername());
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(currentUser.getPassword());
        }else{
            System.out.println("going throw exception");
            throw new UsernameNotFoundException("user not found when load");
        }
        System.out.println("user is going to be built");
        return builder.build();
    }
}
