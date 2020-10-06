package com.softuni.dtos.services.impl;

import com.softuni.dtos.dtos.UserDto;
import com.softuni.dtos.dtos.UserLoginDto;
import com.softuni.dtos.dtos.UserRegisterDto;
import com.softuni.dtos.entities.Role;
import com.softuni.dtos.entities.User;
import com.softuni.dtos.repositories.UserRepository;
import com.softuni.dtos.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private User loggedUser;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        User userEntity = modelMapper.map(userRegisterDto, User.class);
        userEntity.setRole(this.userRepository.count()>0 ? Role.USER : Role.ADMIN);
        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public User checkUserByCredentials(UserLoginDto userLoginDto) {
        return this.userRepository.findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword());
    }

    @Override
    @Transactional
    public UserDto loginUser(UserLoginDto userLoginDto) {
        this.userRepository.updateLoginStatus(userLoginDto.getEmail(), userLoginDto.getPassword(), true);
        return modelMapper.map(this.userRepository.findByLogged(true), UserDto.class);
    }

    @Override
    @Transactional
    public void logoutUser() {
        User foundUser =  this.userRepository.findByLogged(true);
        if(foundUser != null){
            this.userRepository.updateLoginStatus(foundUser.getEmail(), foundUser.getPassword(), false);
            System.out.printf("User %s successfully logged out.%n", foundUser.getFullName());
        } else System.out.println("Cannot log out. No user was logged in.");
    }

    @Override
    public UserDto getLoggedUser() {
       User user =  this.userRepository.findByLogged(true);
       return user != null ? modelMapper.map(user, UserDto.class) : null;
    }

    @Override
    public void updateUser(UserDto user) {
        this.userRepository.saveAndFlush(modelMapper.map(user, User.class));
    }

    @Override
    public boolean isAdminLogged() {
        UserDto loggedUser = this.getLoggedUser();
        if (!(loggedUser != null && loggedUser.getRole() == Role.ADMIN)) {
            System.out.println("You dont have enough privileges to add/edit/remove games to catalog. (ADMIN access required)");
            return false;
        }
        return true;
    }
}
