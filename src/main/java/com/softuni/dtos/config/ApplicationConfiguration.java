package com.softuni.dtos.config;

import com.softuni.dtos.utils.ConsoleUtil;
import com.softuni.dtos.utils.ValidationUtil;
import com.softuni.dtos.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolation;
import java.util.Scanner;
import java.util.Set;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }

    @Bean
    public ValidationUtil validationUtil(){
        return new ValidationUtilImpl();
    }

    @Bean
    public ConsoleUtil consoleUtil(){
        return new ConsoleUtil();
    }
}
