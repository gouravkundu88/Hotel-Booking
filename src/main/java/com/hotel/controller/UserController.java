package com.hotel.controller;

import com.hotel.dto.LoginDto;
import com.hotel.dto.PropertyUserDto;
import com.hotel.entity.PropertyUser;
import com.hotel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto propertyUserDto){
        PropertyUser propertyUser = userService.addUser(propertyUserDto);
        if (propertyUser!=null){
            return new ResponseEntity<>("Registration is Sucessful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto ){
       String token = userService.verifyLogin(loginDto);
       if (token!=null){
           return new ResponseEntity<>(token,HttpStatus.OK);
       }
        return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);

    }
}
