//package com.jeewaloka.digital.jeewalokadigital.configaration;
//
//import com.jeewaloka.digital.jeewalokadigital.filter.JwtAuthenticationFilter;
//import com.jeewaloka.digital.jeewalokadigital.service.Security.UserDetailsServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final UserDetailsServiceImpl userDetailsServiceImpl;
////    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    public SecurityConfig(
//            JwtAuthenticationFilter jwtAuthFilter,
//            UserDetailsServiceImpl userDetailsServiceImpl
////            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
//    ) {
//        this.jwtAuthFilter = jwtAuthFilter;
//        this.userDetailsServiceImpl = userDetailsServiceImpl;
////        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//    }
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf().disable()
////                .authorizeHttpRequests()
////                .requestMatchers("/api/auth/**").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .authenticationProvider(authenticationProvider())
////                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http
//            .cors(Customizer.withDefaults())
//            .csrf(csrf -> csrf.disable())  // Updated CSRF configuration
////            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                    .requestMatchers(HttpMethod.POST,"/api/auth/login", "/api/auth/refresh-token").permitAll()  // Whitelist auth endpoints
////                    .requestMatchers(HttpMethod.GET,"/api/auth/**").hasAuthority("CASHIER")
//                    .requestMatchers("/api/auth/**").hasAuthority("ROLE_ADMIN")
////                    .requestMatchers(HttpMethod.GET,"/api/users/**", "/api/userCreds/**").permitAll()
//                    .requestMatchers(HttpMethod.GET,"/api/users/getusers", "/api/userCreds/getUserCreds").hasRole("ADMIN")
//                    .anyRequest().authenticated()                 // Secure all other endpoints
//            )
//            .sessionManagement(session -> session
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No sessions
//            )
//            .authenticationProvider(authenticationProvider())
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//            .build();
//}
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsServiceImpl);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}

package com.jeewaloka.digital.jeewalokadigital.configaration;

import com.jeewaloka.digital.jeewalokadigital.filter.JwtAuthenticationFilter;
import com.jeewaloka.digital.jeewalokadigital.service.Security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint; // Import AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint; // Import HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            UserDetailsServiceImpl userDetailsServiceImpl
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login", "/api/auth/refresh-token").permitAll()
                        .requestMatchers("/ws/**").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/presence/online").hasAuthority("ROLE_CASHIER")
                        .requestMatchers("/api/presence/online","/api/presence/online").hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER","ROLE_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/auth/sessions").hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER","ROLE_MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/api/auth/revoke-session","/api/auth/revokeAllSessions").hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER","ROLE_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/items/getitems","/api/retailer/getAllRetailers","/api/userCreds/findUserID/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER")
                        .requestMatchers("/api/items/**","/api/suppliers/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/grns/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER")
                        .requestMatchers("/api/Bill/**","/api/BillItem/**","/api/retailer/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER")
//                                .requestMatchers().hasAnyAuthority("ROLE_ADMIN","ROLE_CASHIER")
                        .requestMatchers("/api/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers()
//                        .requestMatchers(HttpMethod.GET,"/api/users/getusers", "/api/userCreds/getUserCreds").hasRole("ADMIN")
//                        .requestMatchers("/api/users/**","/api/userCreds/**").hasRole("ADMIN")
                        // Add rules for suppliers and items if they also need ADMIN role
//                        .requestMatchers("/api/suppliers/**", "/api/items/**").hasRole("ADMIN") // <-- ADDED EXAMPLE
//                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // --- Keep other beans (AuthenticationProvider, PasswordEncoder, AuthenticationManager) ---
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}