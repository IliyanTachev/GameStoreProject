package com.softuni.dtos.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegisterDto {
    private String fullName;
    private String email;
    private String password;

    public UserRegisterDto() {
    }

    public UserRegisterDto(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Pattern(regexp = ".+@.+\\.", message = "Email is not valid.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Pattern(regexp="[A-Z]+[a-z]+[0-9]+", message="Password is not valid.")
    @Size(min=6, message = "Password length is not valid (at least 6 digits).")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
