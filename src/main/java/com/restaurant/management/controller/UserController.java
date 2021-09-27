package com.restaurant.management.controller;

import com.restaurant.management.model.User;
import com.restaurant.management.service.UserService;
import com.restaurant.management.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//Controller
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        User savedUser = userService.save(userForm);

        return new ResponseEntity(savedUser, HttpStatus.OK);
    }

    }
