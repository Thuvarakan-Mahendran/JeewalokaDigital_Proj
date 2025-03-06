package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.UserCredentialsDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.service.RefreshTokenRedisService;
import com.jeewaloka.digital.jeewalokadigital.service.Security.JwtService;
import com.jeewaloka.digital.jeewalokadigital.service.Security.UserDetailsServiceImpl;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import com.jeewaloka.digital.jeewalokadigital.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final ModelMapper modelMapper;
    private UserCredService userCredService;
    private final RefreshTokenRedisService refreshTokenRedisService;
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserDetailsServiceImpl userDetailsServiceImpl,
            ModelMapper modelMapper,
            UserCredService userCredService,
            RefreshTokenRedisService refreshTokenRedisService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.modelMapper = modelMapper;
        this.userCredService = userCredService;
        this.refreshTokenRedisService = refreshTokenRedisService;
    }

    @GetMapping("/sessions")
    public ResponseEntity<Map<String, String>> getActiveSessions(@RequestHeader("Authorization") String refreshToken){
        String username = jwtService.extractUsername(refreshToken.substring(7));
        System.out.println(username + " from sessions");
//        if(!refreshTokenRedisService.checkTokenExists(username,refreshToken)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Token is not valid"));
//        }
        Map<String, String> activeSessions = refreshTokenRedisService.getActiveSessions(username);
        return ResponseEntity.ok(activeSessions);
    }

    @DeleteMapping("/revoke-session")
    public ResponseEntity<String> revokeSession(@RequestHeader("Authorization") String refreshToken){
        String username = jwtService.extractUsername(refreshToken.substring(7));
        boolean revoked = refreshTokenRedisService.revokeRefreshToken(username,refreshToken);
        return revoked
                ?ResponseEntity.ok("session revoked successfully")
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found");
    }

    @DeleteMapping("/revokeAllSessions")
    public ResponseEntity<String> revokeAllSessions(@RequestHeader("Authorization") String refreshToken){
        String username = jwtService.extractUsername(refreshToken.substring(7));
        refreshTokenRedisService.revokeAllSessions(username);
        return ResponseEntity.ok("All sesssions revoked successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserCredentialsDTO userCredentialsDTO, HttpServletRequest httpServletRequest) {
        UserCredentials userCredentials = modelMapper.map(userCredentialsDTO,UserCredentials.class);

        String token = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
        );
        if(authentication.isAuthenticated()) {
            UserDetails user = userDetailsServiceImpl.loadUserByUsername(userCredentials.getUsername());
            Map<String, String> tokens = jwtService.generateTokens(user);
            System.out.println("token generated");
            String ip = httpServletRequest.getRemoteAddr();
            String userAgent = httpServletRequest.getHeader("User-Agent");
            refreshTokenRedisService.storeRefreshToken(user.getUsername(),tokens.get("refreshToken"),ip,userAgent);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + tokens.get("accessToken"))
                    .body(tokens.toString() + " " + ip + " " + userAgent);
        }
        return ResponseEntity.ok("failed authentication");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String refreshToken){
        if(refreshToken == null || !refreshToken.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
        String token = refreshToken.substring(7);
        String username = jwtService.extractUsername(token);
        boolean deleted = refreshTokenRedisService.revokeRefreshToken(username,token);
        if(!deleted){
            return ResponseEntity.ok("Token is invalidated already");
        }
        return ResponseEntity.ok("user logged out successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken, HttpServletRequest httpServletRequest){
        if(refreshToken == null || !refreshToken.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
        String token = refreshToken.substring(7);
        String username = jwtService.extractUsername(token);

        String currentIP = httpServletRequest.getRemoteAddr();
        String currentUserAgent = httpServletRequest.getHeader("User-Agent");

        boolean isValid = refreshTokenRedisService.validateRefreshToken(username,token,currentIP,currentUserAgent);
        if(!isValid){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token is invalid");
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
//        if(!jwtService.isTokenValid(token,userDetails)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
//        }
//        Map<String, String> newTokens = jwtService.generateTokens(userDetails);
        String newToken = jwtService.generateToken(new HashMap<>(),userDetails,1000*20);
        return ResponseEntity.ok(newToken);
    }

    @GetMapping("/usercred/{id}")
    public ResponseEntity<UserCredentialsDTO> getUserCredentials(@PathVariable Long id){
        UserCredentialsDTO userCredentialsDTO = modelMapper.map(userCredService.findById(id),UserCredentialsDTO.class);
        ResponseEntity<UserCredentialsDTO> responseEntity = new ResponseEntity<>(userCredentialsDTO,HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/createuser")
    public String addUserCredentials(@RequestBody UserCredentialsDTO userCredentialsDTO){
        userCredService.addUserCredentials(userCredentialsDTO);
        return "user has been created successfully";
    }
}
