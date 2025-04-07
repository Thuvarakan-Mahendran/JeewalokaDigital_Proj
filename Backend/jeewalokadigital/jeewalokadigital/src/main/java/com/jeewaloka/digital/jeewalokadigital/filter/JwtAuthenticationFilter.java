package com.jeewaloka.digital.jeewalokadigital.filter;

import com.jeewaloka.digital.jeewalokadigital.service.Security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        if (authHeader == null || !authHeader.startsWith("Bearer ") || request.getServletPath().equals("/api/auth/refresh-token") || request.getServletPath().startsWith("/ws")) {
            System.out.println("inside if of filter");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userName = jwtService.extractUsername(jwt);
        System.out.println("auth filter inside");
        if(jwtService.isTokenExpired(jwt)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token expired");
            return;
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Not to authenticate the user who had been authenticated already
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("token got validated");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
//            else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token invalid");
//                return;
//            }
        }
        System.out.println("end of auth filter");
        filterChain.doFilter(request, response);
    }
}

//package com.jeewaloka.digital.jeewalokadigital.filter;
//
//import com.jeewaloka.digital.jeewalokadigital.service.Security.JwtService;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.security.SignatureException; // Correct import for jjwt >= 0.10.x
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.BadCredentialsException; // Import this
//import org.springframework.security.authentication.CredentialsExpiredException; // Import this
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//
//    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
//            "/api/auth/login",
//            "/api/auth/refresh-token"
//    );
//
//    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        final String servletPath = request.getServletPath();
//        boolean isExcludedPath = EXCLUDED_PATHS.stream().anyMatch(path -> servletPath.startsWith(path));
//        if (isExcludedPath) {
//            System.out.println("JWT Filter: Skipping excluded path: " + servletPath);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        String userName = null;
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//
//        try {
//            // This might throw JwtException (Expired, Malformed, Signature etc)
//            userName = jwtService.extractUsername(jwt);
//
//            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
//
//                // isTokenValid internally checks expiry & signature again.
//                // Let it throw exceptions if invalid.
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    System.out.println("JWT Filter: Token validated for user: " + userName);
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities()
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//                // No 'else' needed if isTokenValid throws exceptions for invalid cases
//            }
//            // ---> MODIFIED CATCH BLOCKS <---
//        } catch (ExpiredJwtException e) {
//            System.out.println("JWT Filter: Token expired - " + e.getMessage());
//            // Throw an AuthenticationException that ExceptionTranslationFilter understands
//            // This will lead to AuthenticationEntryPoint (401)
//            SecurityContextHolder.clearContext(); // Good practice to clear context
//            // Let ExceptionTranslationFilter handle this by throwing an AuthenticationException
//            // We don't throw directly here because the filter chain needs to continue
//            // to reach ExceptionTranslationFilter *after* this filter finishes.
//            // Just ensure context is clear and proceed. The lack of Authentication
//            // combined with the prior exception often leads ExceptionTranslationFilter
//            // to trigger the AuthenticationEntryPoint.
//            // However, to be MORE EXPLICIT and guarantee 401, we could try setting
//            // a request attribute that a later filter or the entry point checks.
//            // Let's stick to the simpler "clear context and proceed" first.
//            // If that *still* results in 403, the alternative is needed.
//
//            // **Correction:** The best practice IS to let ExceptionTranslationFilter handle it,
//            // but simply continuing might not trigger the right path.
//            // Setting the error on the response is bypassing Spring Security.
//            // Let's explicitly throw the correct Spring Security exception.
//            throw new CredentialsExpiredException("JWT Token has expired", e);
//
//
//        } catch (SignatureException | MalformedJwtException | IllegalArgumentException e) {
//            // Catch specific JWT format/signature errors
//            System.err.println("JWT Filter: Invalid JWT Token - " + e.getMessage());
//            SecurityContextHolder.clearContext();
//            // Throw an AuthenticationException that ExceptionTranslationFilter understands
//            // This will lead to AuthenticationEntryPoint (401)
//            throw new BadCredentialsException("Invalid JWT Token: " + e.getMessage(), e);
//
//        } catch (JwtException e) {
//            // Catch any other JWT-related exceptions
//            System.err.println("JWT Filter: JWT Exception - " + e.getMessage());
//            SecurityContextHolder.clearContext();
//            throw new BadCredentialsException("JWT processing error: " + e.getMessage(), e);
//
//        } catch (UsernameNotFoundException e) {
//            System.err.println("JWT Filter: User not found - " + e.getMessage());
//            SecurityContextHolder.clearContext();
//            throw new BadCredentialsException("User details not found for token", e); // Or handle as appropriate
//
//        }
//        // Let other exceptions propagate if necessary
//
//        System.out.println("JWT Filter: Continuing filter chain for path: " + servletPath);
//        filterChain.doFilter(request, response);
//    }
//}