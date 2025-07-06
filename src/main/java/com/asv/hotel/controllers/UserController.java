package com.asv.hotel.controllers;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        UserDTO newuserDTO=userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newuserDTO);
    }



}
