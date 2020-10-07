package com.softuni.dtos.dtos;

import com.softuni.dtos.entities.Game;
import com.softuni.dtos.entities.Order;
import com.softuni.dtos.entities.Role;

import java.util.Set;

public class UserDto {
    private long id;
    private String fullName;
    private String email;
    private String password;
    private Set<GameDto> games;
    private Role role;
    private Set<OrderDto> orders;
    private Set<GameDto> shoppingCart;
    private boolean logged;

    public UserDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<GameDto> getGames() {
        return games;
    }

    public void setGames(Set<GameDto> games) {
        this.games = games;
    }

    public Set<GameDto> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Set<GameDto> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderDto> orders) {
        this.orders = orders;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
