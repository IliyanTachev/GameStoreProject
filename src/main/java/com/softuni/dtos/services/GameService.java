package com.softuni.dtos.services;

import com.softuni.dtos.dtos.DetailGameDto;
import com.softuni.dtos.dtos.GameDto;
import com.softuni.dtos.dtos.ViewGameDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameService {
    void addGame(GameDto gameDto);
    GameDto validateID(long id);
    void updateGame(String params, long gameId);
    void deleteGame(long gameId2);
    List<ViewGameDto> getAllGames();
    List<DetailGameDto> getAllGamesByTitle(String title);
}
