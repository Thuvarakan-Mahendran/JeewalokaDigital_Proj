//package com.jeewaloka.digital.jeewalokadigital.security;
//
//import com.jeewaloka.digital.jeewalokadigital.authentication.CustomAuthenticationSuccessHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
////                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (for testing or specific cases)
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection (for testing or specific cases)
//                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict /admin/** to ADMIN role
////                        .requestMatchers("/public/**").permitAll()    // Allow access to /public/** for everyone
//                        .anyRequest().authenticated() // Require authentication for all requests
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage(
//                                "/login"        //custom login page
//                        )
//                        .successHandler(customAuthenticationSuccessHandler()) // Use the custom success handler
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/") // Redirect to the homepage after logout
//                );
//        return http.build();
//    }
//
//    @Bean
//    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return new CustomAuthenticationSuccessHandler();
//    }
//
//}
