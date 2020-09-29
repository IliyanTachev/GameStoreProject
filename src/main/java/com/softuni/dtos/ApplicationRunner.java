package com.softuni.dtos;;

import com.softuni.dtos.dtos.*;
import com.softuni.dtos.entities.Game;
import com.softuni.dtos.entities.Role;
import com.softuni.dtos.services.GameService;
import com.softuni.dtos.services.UserService;
import com.softuni.dtos.utils.ValidationUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private final ValidationUtil validationUtil;
    private UserService userService;
    private final Scanner scanner;
    private final GameService gameService;

    public ApplicationRunner(ValidationUtil validationUtil, UserService userService, Scanner scanner, GameService gameService) {
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.scanner = scanner;
        this.gameService = gameService;

    }

    @Override
    public void run(String... args) throws Exception {
        while(true){
            System.out.println("Enter a command: ");
            String inputStr = scanner.nextLine();
            String[] input = inputStr.split("|");

            switch(input[0]){
                case "RegisterUser":
                    if(!input[2].equals(input[3])) {
                        System.out.println("Passwords do not match.");
                        break;
                    }
                    UserRegisterDto userRegisterDto = new UserRegisterDto(input[1], input[2], input[4]);
                    if(validationUtil.isValid(userRegisterDto)){
                        this.userService.registerUser(userRegisterDto);
                        System.out.printf("User: %s was registered successfully.", input[4]);
                    } else {
                        this.validationUtil.getViolations(userRegisterDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
                    }
                    break;

                case "LoginUser":
                    UserLoginDto userLoginDto = new UserLoginDto(input[1], input[2]);
                    if(this.validationUtil.isValid(userLoginDto)){
                        if(this.userService.checkUserByCredentials(userLoginDto) != null){
                            UserDto loginUser = this.userService.loginUser(userLoginDto);
                            System.out.printf("Logged as %s" + loginUser.getFullName());
                        } else {
                            System.out.println("Incorrect username / password");
                            break;
                        }
                    } else {
                        this.validationUtil.getViolations(userLoginDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                    break;

                case "Logout":
                     this.userService.logoutUser();
                    break;

                case "AddGame":
                    if(!isAdminLogged()) break;

                    GameDto gameDto = new GameDto(input[1], new BigDecimal(input[2]), Integer.parseInt(input[3]), input[4], input[5], input[6], LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-mm-yyyy")));
                        if(validationUtil.isValid(gameDto)){
                                this.gameService.addGame(gameDto);
                                System.out.println("Added " + gameDto.getTitle());
                        } else {
                            this.validationUtil.getViolations(gameDto)
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    break;

                    case "EditGame":
                        if(!isAdminLogged()) break;
                        long gameId = Long.parseLong(input[1]);
                        GameDto editGame = new GameDto();
                        if((editGame = this.gameService.validateID(gameId)) != null){
                            String updateStatementParams = "";
                            for(int i=2;i<input.length;i++){
                                updateStatementParams += input[i];
                                if(i < input.length-1) updateStatementParams += ",";
                            }
                            this.gameService.updateGame(updateStatementParams, gameId);
                            System.out.printf("Edited %s (ID=%l)%n", editGame.getTitle(), gameId);
                        } else {
                            System.out.println("There is no game with id=" + gameId);
                        }
                        break;

                    case "DeleteGame":
                        if(!isAdminLogged()) break;
                        long gameId2 = Long.parseLong(input[1]);
                        GameDto deleteGame = new GameDto();
                        if((deleteGame = this.gameService.validateID(gameId2)) != null){
                            this.gameService.deleteGame(gameId2);
                            System.out.printf("Deleted %s (ID=%l)%n",deleteGame.getTitle(), gameId2);
                        } else {
                            System.out.println("There is no game with id=" + gameId2);
                        }
                        break;

                case "AllGames":
                    this.gameService.getAllGames()
                            .stream()
                            .forEach(System.out::println);
                    break;

                case "DetailGame":
                        this.gameService.getAllGamesByTitle(input[1])
                        .stream()
                        .forEach(System.out::println);
                    break;
                case "OwnedGames":
                    UserDto loggedUser = this.userService.getLoggedUser();
                    loggedUser.getGames().stream().map(Game::getTitle).forEach(System.out::println);
                    break;
            }
        }
    }

    private boolean isAdminLogged() {
        UserDto user = new UserDto();
        if (!((user = this.userService.getLoggedUser()) != null && user.getRole() == Role.ADMIN)) {
            System.out.println("You must be logged as <ADMIN> to add a new game.");
            return false;
        }
        return true;
    }
}
