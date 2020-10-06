package com.softuni.dtos.repositories;

import com.softuni.dtos.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Modifying
    @Query(value = "insert into games set title=?1, price=?2, size=?3, trailer=?4, image_thumbnail=?5, description=?6, release_date=?7", nativeQuery = true)
    void insertNewGame(String title, BigDecimal price, double size, String trailer, String imageThumbnail, String description, LocalDate releaseDate);

    /*
    @Modifying
    @Query(value = "update games set :params where id=:gameId", nativeQuery = true)
    void updateGame(@Param(value="params") String params, @Param(value="gameId") long gameId); */
    List<Game> findAllByTitle(String title);
    Game findByTitle(String title);
}
