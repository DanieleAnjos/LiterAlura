package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonAlias("title") String nome,
                         @JsonAlias("authors") Integer anoNascimento,
                         @JsonAlias("languages") Integer anoFalecimento ){
}