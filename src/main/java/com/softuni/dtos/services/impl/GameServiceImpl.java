package com.softuni.dtos.services.impl;

import com.softuni.dtos.dtos.DetailGameDto;
import com.softuni.dtos.dtos.GameDto;
import com.softuni.dtos.dtos.ViewGameDto;
import com.softuni.dtos.entities.Game;
import com.softuni.dtos.repositories.GameRepository;
import com.softuni.dtos.services.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void addGame(GameDto gameDto){
        this.gameRepository.insertNewGame(gameDto.getTitle(), gameDto.getPrice(),
                gameDto.getSize(), gameDto.getTrailer(), gameDto.getImageThumbnail(),
                gameDto.getDescription(), gameDto.getReleaseDate());
    }

    @Override
    public GameDto validateID(long id) {
        var gameFound = this.gameRepository.findById(id).orElse(null);
        if(gameFound != null) return modelMapper.map(gameFound, GameDto.class);
        return null;
    }

    @Override
    @Transactional
    public void updateGame(Map<String, String> params, long gameId) {
        Game game = this.gameRepository.findById(gameId).orElse(null);
        System.out.println();

        for(String key : params.keySet()) {
            if (key.equals("title")) {
                game.setTitle(params.get("title"));
            } else if (key.equals("price")) {
                game.setPrice(new BigDecimal(params.get("price")));
            } else if (key.equals("size")) {
                game.setSize(Double.parseDouble(params.get("size")));
            } else if (key.equals("trailer")) {
                game.setTrailer(params.get("trailer"));
            } else if (key.equals("thumbnail")) {
                game.setImageThumbnail(params.get("thumbnail"));
            } else if (key.equals("description")) {
                game.setDescription(params.get("description"));
            }
        }
        this.gameRepository.saveAndFlush(game);
    }

    @Override
    public void deleteGame(long gameId2) {
        this.gameRepository.deleteById(gameId2);
    }

    @Override
    public List<ViewGameDto> getAllGames() {
        return this.gameRepository.findAll().stream()
                .map(game -> modelMapper.map(game, ViewGameDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DetailGameDto getGameDetailByTitle(String title) {
       return retrieveGameResult(new DetailGameDto(), title, t -> this.gameRepository.findByTitle(t));
    }

    @Override
    public GameDto getGameByTitle(String title) {
        return retrieveGameResult(new GameDto(), title, t -> this.gameRepository.findByTitle(t));
    }

    private <T> T retrieveGameResult(T dtoClass, String searchProperty, Function<String,Game> func){
        Game gameFound = func.apply(searchProperty);
        if(gameFound != null) return modelMapper.map(gameFound, (Type) dtoClass.getClass());
        return null;
    }
}
