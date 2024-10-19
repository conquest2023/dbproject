package com.example.dbproject.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class EditArticleDTO {


    Optional<String> title;


    Optional<String> content;


}
