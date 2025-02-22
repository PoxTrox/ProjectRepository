package org.example.projectRepository.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.example.projectRepository.author.model.Author;
import org.example.projectRepository.user.model.User;

import java.math.BigDecimal;

@Data
public class BookAuthorRequest {



    @NotNull(message = "Please enter title")
    private String title;

    private Author author;

    private BigDecimal price;

    private User user;


}
