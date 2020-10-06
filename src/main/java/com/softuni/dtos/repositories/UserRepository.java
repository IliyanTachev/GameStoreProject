package com.softuni.dtos.repositories;

import com.softuni.dtos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    @Modifying
    @Query("update User as u set u.logged=?3 where u.email=?1 and u.password=?2")
    int updateLoginStatus(String email, String password, boolean status);
    User findByLogged(boolean status);
}
