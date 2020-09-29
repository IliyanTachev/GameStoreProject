package com.softuni.dtos.services;

import com.softuni.dtos.dtos.UserDto;
import com.softuni.dtos.dtos.UserLoginDto;
import com.softuni.dtos.dtos.UserRegisterDto;
import com.softuni.dtos.entities.User;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);
    User checkUserByCredentials(UserLoginDto userLoginDto);
    UserDto loginUser(UserLoginDto userLoginDto);
    void logoutUser();
    UserDto getLoggedUser();
}
