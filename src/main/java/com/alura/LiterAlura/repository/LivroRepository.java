package com.alura.LiterAlura.repository;

import com.alura.LiterAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    boolean existsByTituloContainingIgnoreCase(String titulo);

    boolean existsByTitulo(String titulo);
}
