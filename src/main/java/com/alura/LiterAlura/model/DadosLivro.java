package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro (
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DadosAutor> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDownloads) {

    @Override
    public String toString() {
        StringBuilder autoresFormatados = new StringBuilder();
        for (DadosAutor autor : autores) {
            autoresFormatados.append(autor.toString()).append("\n");
        }

        if (autoresFormatados.length() > 0) {
            autoresFormatados.deleteCharAt(autoresFormatados.length() - 2);
        }

        return " ====== Livro ======= \n" +
                "ID: " + id + "\n" +
                "Titulo: " + titulo + "\n" +
                "Autores: " + autoresFormatados + "\n" +
                "Idiomas: " + idiomas + "\n" +
                "Numero de downloads: " + numeroDownloads;
    }
}
