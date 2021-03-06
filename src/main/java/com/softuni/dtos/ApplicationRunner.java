package com.softuni.dtos;;

import com.softuni.dtos.dtos.*;
import com.softuni.dtos.services.GameService;
import com.softuni.dtos.services.UserService;
import com.softuni.dtos.utils.ConsoleUtil;
import com.softuni.dtos.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final Scanner scanner;
    private final GameService gameService;
    private final ModelMapper modelMapper;
    private final ConsoleUtil consoleUtil;
    private UserDto loggedUser = new UserDto();

    @Autowired
    public ApplicationRunner(ValidationUtil validationUtil, UserService userService, Scanner scanner, GameService gameService, ModelMapper modelMapper, ConsoleUtil consoleUtil) {
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.scanner = scanner;
        this.gameService = gameService;
        this.modelMapper = modelMapper;
        this.consoleUtil = consoleUtil;
    }

    @Override
    public void run(String... args) throws Exception {
        while(true){
            System.out.println("Enter a command: ");
            String inputStr = scanner.nextLine();
            String[] input = inputStr.split("\\|");

            switch(input[0]){
                case "RegisterUser":
                    if(!input[2].equals(input[3])) {
                        ConsoleUtil.printInYellow("Passwords do not match.");
                        break;
                    }
                    UserRegisterDto userRegisterDto = new UserRegisterDto(input[1], input[2], input[4]);
                    if(validationUtil.isValid(userRegisterDto)){
                        this.userService.registerUser(userRegisterDto);
                        ConsoleUtil.printInGreen("User: " + input[4] + " was registered successfully.");
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
                            loggedUser = this.userService.loginUser(userLoginDto);
                            ConsoleUtil.printInGreen("Logged as" + loggedUser.getFullName());
                        } else {
                            ConsoleUtil.printInYellow("Incorrect username / password");
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
                    if(this.userService.isAdminLogged()) {
                        GameDto gameDto = new GameDto(input[1], new BigDecimal(input[2]), Double.parseDouble(input[3]), input[4], input[5], input[6], LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                        if (validationUtil.isValid(gameDto)) {
                            this.gameService.addGame(gameDto);
                            ConsoleUtil.printInGreen("Added " + gameDto.getTitle());
                        } else {
                            this.validationUtil.getViolations(gameDto)
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    }
                    break;

                    case "EditGame":
                        if(this.userService.isAdminLogged()) {
                            long gameId = Long.parseLong(input[1]);
                            GameDto editGame = this.gameService.validateID(gameId);
                            System.out.println();
                            if (editGame != null) {
                                Map<String, String> updateStatementParams = new HashMap<>();
                                for (int i = 2; i < input.length; i++) {
                                    String[] temp = input[i].split("=");
                                    updateStatementParams.put(temp[0], temp[1]);
                                }
                                this.gameService.updateGame(updateStatementParams, gameId);
                                ConsoleUtil.printInGreen("Edited " + editGame.getTitle() + " (ID=" + gameId + ")");
                            } else {
                                ConsoleUtil.printInYellow("There is no game with id=" + gameId);
                            }
                        }
                        break;

                    case "DeleteGame":
                        if(this.userService.isAdminLogged()) {
                            long gameId2 = Long.parseLong(input[1]);
                            GameDto deleteGame = new GameDto();
                            if ((deleteGame = this.gameService.validateID(gameId2)) != null) {
                                this.gameService.deleteGame(gameId2);
                                ConsoleUtil.printInRed("Deleted " + deleteGame.getTitle() + " (ID=" + gameId2 + ")%n");
                            } else {
                                ConsoleUtil.printInYellow("There is no game with id=" + gameId2);
                            }
                        }
                        break;

                case "AllGames":
                    this.gameService.getAllGames()
                            .stream()
                            .forEach(System.out::println);
                    break;
                case "DetailGame":
                        System.out.println(this.gameService.getGameByTitle(input[1]) == null ? "This game was not found. Please try another one." : this.gameService.getGameDetailByTitle(input[1]));
                    break;
                case "OwnedGames":
                    UserDto loggedUser = this.userService.getLoggedUser();
                    if(loggedUser != null) {
                        if(loggedUser.getGames().size() == 0)  ConsoleUtil.printInYellow("You don't own any games.");
                        loggedUser.getGames().stream().map(GameDto::getTitle).forEach(System.out::println);
                    }

                    break;

                case "AddItem":
                    if((loggedUser = this.userService.getLoggedUser()) == null){
                        ConsoleUtil.printInYellow("You must be logged in to add a game to shopping cart.");
                        break;
                    }

                    String gameTitleToAdd = input[1];
                    if (this.gameService.getGameByTitle(gameTitleToAdd) == null) {
                        ConsoleUtil.printInYellow("Game title does not exist.");
                        break;
                    }
                    Set<GameDto> userGames = this.userService.getLoggedUser().getGames();
                    for(GameDto game : userGames){
                        if(game.getTitle().equals(gameTitleToAdd)) {
                            ConsoleUtil.printInYellow("This game is already in your games inventory.");
                            break;
                        }
                    }
                    for (GameDto game : loggedUser.getShoppingCart()){
                        if(game.getTitle().equals(gameTitleToAdd)){
                            ConsoleUtil.printInYellow("This game is already in your shopping cart. Cannot be added twice.");
                            break;
                        }
                    }

                    loggedUser.getShoppingCart().add(this.gameService.getGameByTitle(gameTitleToAdd));
                    this.userService.updateUser(loggedUser);
                    ConsoleUtil.printInGreen(gameTitleToAdd + " added to cart.");

                    break;

                case "RemoveItem":
                    if((loggedUser = this.userService.getLoggedUser()) == null){
                        ConsoleUtil.printInYellow("You must be logged in to add a game to shopping cart.");
                        break;
                    }

                    String gameTitleToRemove = input[1];
                    boolean isGameInShoppingCart = false;
                    for (GameDto game : loggedUser.getShoppingCart()) {
                        if (game.getTitle().equals(gameTitleToRemove)) {
                            isGameInShoppingCart = true;
                            break;
                        }
                    }

                    if (isGameInShoppingCart) {
                        loggedUser.getShoppingCart().removeIf(g -> g.getTitle().equals(gameTitleToRemove));
                        this.userService.updateUser(loggedUser);
                        ConsoleUtil.printInRed(gameTitleToRemove + " removed from cart.");
                    } else ConsoleUtil.printInYellow("Cannot remove game. Game not persists in shopping cart.");

                    break;
                case "BuyItem":
                    if((loggedUser = this.userService.getLoggedUser()) == null){
                        ConsoleUtil.printInYellow("You must be logged in to buy games.");
                        break;
                    }
                    UserDto user = this.userService.getLoggedUser();
                    Set<GameDto> games = user.getGames();
                    for(GameDto game : loggedUser.getShoppingCart()){
                        games.add(game);
                    }
                    user.setGames(games);
                    this.userService.updateUser(user);
                    ConsoleUtil.printInGreen("Successfully bought games:");
                    loggedUser.getShoppingCart().forEach(g->System.out.println(" -" + g.getTitle()));
                    break;

                case "Show shopping cart":
                    if((loggedUser = this.userService.getLoggedUser()) == null){
                        ConsoleUtil.printInYellow("You must be logged in to add a game to shopping cart.");
                        break;
                    }

                    if(loggedUser.getShoppingCart().size() != 0) {
                        System.out.println(" /Shopping cart: ");
                        loggedUser.getShoppingCart().forEach(g-> System.out.println(g.getTitle()));
                    }
                    else ConsoleUtil.printInYellow("Shopping cart is empty.");
                    break;
                default:
                    ConsoleUtil.printInRed("This command in invalid. Please enter a valid command.");
            }
        }
    }
}
