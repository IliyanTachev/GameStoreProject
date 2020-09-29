package com.softuni.dtos.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserLoginDto {
    private String email;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
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
