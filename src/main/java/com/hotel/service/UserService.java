package com.hotel.service;

import com.hotel.dto.LoginDto;
import com.hotel.dto.PropertyUserDto;
import com.hotel.entity.PropertyUser;
import com.hotel.repository.PropertyUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private PropertyUserRepository userRepository;
    private JWTService jwtService;

    public UserService(PropertyUserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setEmail(propertyUserDto.getEmail());
        user.setUsername(propertyUserDto.getUsername());
        //For Password Encryption use this line of code
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(),BCrypt.gensalt(10)));
        user.setUserRole(propertyUserDto.getUserRole());

        PropertyUser savedUser = userRepository.save(user);
        return savedUser;
    }
    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if (opUser.isPresent()){
            PropertyUser propertyUser = opUser.get();
            //For Password Decryption use this line of code
            if(BCrypt.checkpw(loginDto.getPassword(),propertyUser.getPassword())){
                return jwtService.generateToken(propertyUser);
            }
        }
        return null;
    }
}

