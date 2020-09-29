package com.softuni.dtos.services.impl;

import com.softuni.dtos.dtos.DetailGameDto;
import com.softuni.dtos.dtos.GameDto;
import com.softuni.dtos.dtos.ViewGameDto;
import com.softuni.dtos.repositories.GameRepository;
import com.softuni.dtos.services.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    public void addGame(GameDto gameDto){
        this.gameRepository.insertNewGame(gameDto.getTitle(), gameDto.getPrice(),
                gameDto.getSize(), gameDto.getTrailer(), gameDto.getImageThumbnail(),
                gameDto.getDescription(), gameDto.getReleaseDate());
    }

    @Override
    public GameDto validateID(long id) {
        return modelMapper.map(this.gameRepository.findById(id), GameDto.class);
    }

    @Override
    public void updateGame(String params, long gameId) {
        this.gameRepository.updateGame(params, gameId);
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
    public List<DetailGameDto> getAllGamesByTitle(String title) {
        return this.gameRepository.findAllByTitle(title)
                .stream()
                .map(game -> modelMapper.map(game, DetailGameDto.class))
                .collect(Collectors.toList());
    }
}
