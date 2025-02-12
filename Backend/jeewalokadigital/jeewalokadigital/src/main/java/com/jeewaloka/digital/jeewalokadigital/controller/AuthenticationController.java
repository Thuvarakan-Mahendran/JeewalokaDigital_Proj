package com.jeewaloka.digital.jeewalokadigital.controller;


import com.jeewaloka.digital.jeewalokadigital.dto.UserCredentialsDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.service.JwtService;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import com.jeewaloka.digital.jeewalokadigital.service.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private ModelMapper modelMapper;
    private UserCredService userCredService;
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserDetailsServiceImpl userDetailsServiceImpl,
            ModelMapper modelMapper,
            UserCredService userCredService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.modelMapper = modelMapper;
        this.userCredService = userCredService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserCredentialsDTO userCredentialsDTO) {
//        System.out.println("entry");
        UserCredentials userCredentials = modelMapper.map(userCredentialsDTO, UserCredentials.class);
//        System.out.println(userCredentials.getPassword());

        String token = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
        );
        System.out.println("authmanager");
        if(authentication.isAuthenticated()) {
            System.out.println("needed to load username");
            UserDetails user = userDetailsServiceImpl.loadUserByUsername(userCredentials.getUsername());
            System.out.println("loaded username");
            token = jwtService.generateToken(user);
            System.out.println("token is generated");
        }
        return token != null? ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build() : ResponseEntity.ok("failed authentication");
    }

    @GetMapping("/usercred/{id}")
    public ResponseEntity<UserCredentialsDTO> getUserCredentials(@PathVariable Long id){
        UserCredentialsDTO userCredentialsDTO = modelMapper.map(userCredService.findById(id), UserCredentialsDTO.class);
        ResponseEntity<UserCredentialsDTO> responseEntity = new ResponseEntity<>(userCredentialsDTO,HttpStatus.OK);
        return responseEntity;
    }
}
