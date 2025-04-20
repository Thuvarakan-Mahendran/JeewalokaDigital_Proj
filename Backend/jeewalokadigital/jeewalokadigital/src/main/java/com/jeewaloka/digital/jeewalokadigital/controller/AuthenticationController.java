package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.UserCredentialsRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.UserCredentials;
import com.jeewaloka.digital.jeewalokadigital.service.Security.RefreshTokenRedisService;
import com.jeewaloka.digital.jeewalokadigital.service.Security.JwtService;
import com.jeewaloka.digital.jeewalokadigital.service.Security.UserDetailsServiceImpl;
import com.jeewaloka.digital.jeewalokadigital.service.UserCredService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin("")
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
    public ResponseEntity<?> getActiveSessions(@RequestHeader("Authorization") String accessToken){
        String username = jwtService.extractUsername(accessToken.substring(7));
        System.out.println(username + " from sessions");
        List<Map<Object, Object>> activeSessions = refreshTokenRedisService.getActiveSessions(username);
        return ResponseEntity.ok(activeSessions);
    }

//    @DeleteMapping("/revoke-session")
//    public ResponseEntity<String> revokeSession(@RequestHeader("Authorization") String refreshToken){       //need to send refresh token, modification needed
//        String username = jwtService.extractUsername(refreshToken.substring(7));
//        boolean revoked = refreshTokenRedisService.revokeRefreshToken(username,refreshToken);
//        return revoked
//                ?ResponseEntity.ok("session revoked successfully")
//                :ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found");
//    }

    @DeleteMapping("/revoke-session")
    public ResponseEntity<String> revokeSession(HttpServletRequest request,HttpServletResponse response){       //need to send refresh token, modification needed
        System.out.println("inside revoke session");
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                    System.out.println("refresh toke is " + refreshToken);
                    break;
                }
            }
        }
        if(refreshToken == null){
            System.out.println("refresh token is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken.substring(7));
        System.out.println("username is " + username);
        boolean revoked = refreshTokenRedisService.revokeRefreshToken(username,refreshToken);
        return revoked
                ?ResponseEntity.ok("session revoked successfully")
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found");
    }

    @DeleteMapping("/revokeAllSessions")
    public ResponseEntity<String> revokeAllSessions(@RequestHeader("Authorization") String accessToken){
        String username = jwtService.extractUsername(accessToken.substring(7));
        refreshTokenRedisService.revokeAllSessions(username);
        return ResponseEntity.ok("All sesssions revoked successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> authenticate(@RequestBody UserCredentialsRequestDTO userCredentialsDTO, HttpServletRequest httpServletRequest) {
//        UserCredentials userCredentials = modelMapper.map(userCredentialsDTO,UserCredentials.class);
//
//        String token = null;
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
//        );
//        if(authentication.isAuthenticated()) {
//            UserDetails user = userDetailsServiceImpl.loadUserByUsername(userCredentials.getUsername());
//            Map<String, String> tokens = jwtService.generateTokens(user);
//            System.out.println("token generated");
//            String ip = httpServletRequest.getRemoteAddr();
//            String userAgent = httpServletRequest.getHeader("User-Agent");
//            refreshTokenRedisService.storeRefreshToken(user.getUsername(),tokens.get("refreshToken"),ip,userAgent);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + tokens.get("accessToken"))
//                    .body(tokens.toString() + " " + ip + " " + userAgent);
//        }
//        return ResponseEntity.ok("failed authentication");
//    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserCredentialsRequestDTO userCredentialsDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.out.println("inside login");
        UserCredentials userCredentials = modelMapper.map(userCredentialsDTO,UserCredentials.class);

//        String token = null;
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
        );
        if(authentication.isAuthenticated()) {
            UserDetails user = userDetailsServiceImpl.loadUserByUsername(userCredentials.getUsername());
            Map<String, String> tokens = jwtService.generateTokens(user);
            System.out.println("tokens generated");
            System.out.println("refresh token is " + tokens.get("refreshToken"));
            String ip = httpServletRequest.getRemoteAddr();
            String userAgent = httpServletRequest.getHeader("User-Agent");
            System.out.println("ip is " + ip);
            System.out.println("user agent is " + userAgent);
            try {
                refreshTokenRedisService.storeRefreshToken(user.getUsername(), tokens.get("refreshToken"), ip, userAgent);
            } catch (Exception e) {
                System.err.println("Warning: Failed to store refresh token in Redis: " + e.getMessage());
            }
//            refreshTokenRedisService.storeRefreshToken(user.getUsername(),tokens.get("refreshToken"),ip,userAgent);
//            refreshTokenRedisService.storeRefreshToken(user.getUsername(),tokens.get("refreshToken"));
            Cookie refreshTokenCookie = new Cookie("refreshToken",tokens.get("refreshToken"));
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
//            refreshTokenCookie.setPath("/api/auth/refresh-token");
            refreshTokenCookie.setPath("/api/auth");
            refreshTokenCookie.setMaxAge(7*24*60*60);
            httpServletResponse.addCookie(refreshTokenCookie);
            return ResponseEntity.ok()
//                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + tokens.get("accessToken"))
//                    .body(tokens.toString() + " " + ip + " " + userAgent);
                    .body(tokens.get("accessToken"));
        }
        return ResponseEntity.ok("failed authentication");
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String refreshToken){
//        if(refreshToken == null || !refreshToken.startsWith("Bearer ")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
//        }
//        String token = refreshToken.substring(7);
//        String username = jwtService.extractUsername(token);
//        boolean deleted = refreshTokenRedisService.revokeRefreshToken(username,token);
//        if(!deleted){
//            return ResponseEntity.ok("Token is invalidated already");
//        }
//        return ResponseEntity.ok("user logged out successfully");
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request,HttpServletResponse response){
        System.out.println("inside the logout controller");
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        Cookie refreshTokenCookie = new Cookie("refreshToken",null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/api/auth");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        if(refreshToken == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);
        boolean deleted = refreshTokenRedisService.revokeRefreshToken(username,refreshToken);
        if(!deleted){
            return ResponseEntity.ok("Token is invalidated already");
        }
        return ResponseEntity.ok("user logged out successfully");
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken, HttpServletRequest httpServletRequest){
//        if(refreshToken == null || !refreshToken.startsWith("Bearer ")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
//        }
//        String token = refreshToken.substring(7);
//        String username = jwtService.extractUsername(token);
//
//        String currentIP = httpServletRequest.getRemoteAddr();
//        String currentUserAgent = httpServletRequest.getHeader("User-Agent");
//
//        boolean isValid = refreshTokenRedisService.validateRefreshToken(username,token,currentIP,currentUserAgent);
//        if(!isValid){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token is invalid");
//        }
//
//        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
////        if(!jwtService.isTokenValid(token,userDetails)){
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
////        }
////        Map<String, String> newTokens = jwtService.generateTokens(userDetails);
//        String newToken = jwtService.generateToken(new HashMap<>(),userDetails,1000*20);
//        return ResponseEntity.ok(newToken);
//    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        Cookie[] cookies = httpServletRequest.getCookies();
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                    System.out.println("refresh token is " + refreshToken);
                    break;
                }
            }
        }
        if(refreshToken == null){
            System.out.println("refresh token is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);
        System.out.println("refresh token received is " + refreshToken);
        System.out.println("user name extracted is " + username);
        String currentIP = httpServletRequest.getRemoteAddr();
        System.out.println("ip address of system is " + currentIP);
        String currentUserAgent = httpServletRequest.getHeader("User-Agent");
        System.out.println("user agent is " + currentUserAgent);
//        boolean isValid = refreshTokenRedisService.validateRefreshToken(username,refreshToken,currentIP,currentUserAgent);
        boolean isValid = refreshTokenRedisService.validateRefreshToken(username,refreshToken);
        System.out.println("token is " + isValid);
        if(!isValid){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token is invalid");
        }
        System.out.println("refresh token got validated with redis storage");
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
//        if(!jwtService.isTokenValid(token,userDetails)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
//        }
//        Map<String, String> newTokens = jwtService.generateTokens(userDetails);
        System.out.println("user details are " + userDetails.getUsername() + " " + userDetails.getPassword() + " " + userDetails.getAuthorities());
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        String newToken = jwtService.generateToken(claims,userDetails,1000*30);
        return ResponseEntity.ok(newToken);
    }

    @GetMapping("/usercred/{id}")
    public ResponseEntity<UserCredentialsRequestDTO> getUserCredentials(@PathVariable Long id){
        UserCredentialsRequestDTO userCredentialsDTO = modelMapper.map(userCredService.findById(id), UserCredentialsRequestDTO.class);
        ResponseEntity<UserCredentialsRequestDTO> responseEntity = new ResponseEntity<>(userCredentialsDTO,HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/createuser")
    public String addUserCredentials(@RequestBody UserCredentialsRequestDTO userCredentialsDTO){
        userCredService.addUserCredentials(userCredentialsDTO);
        return "user has been created successfully";
    }
}
