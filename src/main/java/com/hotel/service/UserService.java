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

    public UserService(PropertyUserRepository userRepository) {
        this.userRepository = userRepository;
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
    public boolean verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> opUser = userRepository.findByUsername(loginDto.getUsername());
        if (opUser.isPresent()){
            PropertyUser propertyUser = opUser.get();
            //For Password Decryption use this line of code
            return BCrypt.checkpw(loginDto.getPassword(),propertyUser.getPassword());
        }
        return false;
    }
}

