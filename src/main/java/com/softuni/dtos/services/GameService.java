package com.softuni.dtos.services;

import com.softuni.dtos.dtos.DetailGameDto;
import com.softuni.dtos.dtos.GameDto;
import com.softuni.dtos.dtos.ViewGameDto;
import java.util.List;
import java.util.Map;

public interface GameService {
    void addGame(GameDto gameDto);
    GameDto validateID(long id);
    void updateGame(Map<String, String> params, long gameId);
    void deleteGame(long gameId2);
    List<ViewGameDto> getAllGames();
    DetailGameDto getGameDetailByTitle(String title);
    GameDto getGameByTitle(String title);
}
