package com.hotel.config;

import com.hotel.entity.PropertyUser;
import com.hotel.repository.PropertyUserRepository;
import com.hotel.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    
    private JWTService jwtService;
    
    private PropertyUserRepository userRepository;

    public JWTRequestFilter(JWTService jwtService, PropertyUserRepository propertyUserRepository) {
        this.jwtService = jwtService;
        this.userRepository = propertyUserRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader!= null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(8, tokenHeader.length()-1);
            String username = jwtService.getUsername(token);
            Optional<PropertyUser> opUser = userRepository.findByUsername(username);
            if (opUser.isPresent()){
                PropertyUser user = opUser.get();
                //To keep track of current user logged in
                //For Creating Session We have to use this line of Code
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
                        (user, null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
