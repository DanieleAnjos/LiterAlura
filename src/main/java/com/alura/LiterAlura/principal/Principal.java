package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.Autor;
import com.alura.LiterAlura.model.DadosAutor;
import com.alura.LiterAlura.model.DadosLivro;
import com.alura.LiterAlura.model.Livro;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LivroRepositoty;
import com.alura.LiterAlura.service.ConsumoApi;
import com.alura.LiterAlura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com";

    @Autowired
    private LivroRepositoty repositorio;
    private List<Livro> livros = new ArrayList<>();
    private Optional<Livro> buscaLivro;

    @Autowired
    private AutorRepository repository;
    private List<Autor> autores = new ArrayList<>();
    private Optional<Autor> buscaAutor;

    public void exibirMenu() {
        while (true) {
            System.out.println("Escolha uma opção: \n");
            System.out.println("1 - Buscar livro pelo título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Buscar autores registrados");
            System.out.println("4 - Listar autores vivos em determinado ano");
            System.out.println("5 - Listar livros em determinado idioma");
            System.out.println("0 - Sair");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listaLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    buscarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private void listaLivros() {
        livros = repositorio.findAll();
        livros.forEach(System.out::println);
    }

    public DadosLivro getDadosLivro() {
        System.out.println("Digite o nome do livro: ");
        var livro = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + "/books?search=" + livro.replace(" ", "+"));
        DadosLivro dados = converteDados.obterDados(json, DadosLivro.class);
        return dados;
    }

    private void buscarLivro() {
        try {
            DadosLivro dados = getDadosLivro();
            if (!repositorio.existsByTitulo(dados.titulo())) {
                Livro livro = new Livro(dados);
                repositorio.save(livro);
            }
            System.out.println(dados);
        } catch (Exception e) {
            System.err.println("Erro ao buscar livro: " + e.getMessage());
        }
    }

    private void listarAutores() {
        autores = repository.findAll();
        autores.forEach(System.out::println);
    }

    private void buscarAutoresVivosPorAno() {
        try {
            System.out.println("Digite o ano a ser buscado: ");
            var ano = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            var json = consumo.obterDados(ENDERECO + "/books?author_year_start=" + ano);
            DadosAutor dados = converteDados.obterDados(json, DadosAutor.class);
            System.out.println(dados);
        } catch (Exception e) {
            System.err.println("Erro ao buscar autores por ano: " + e.getMessage());
        }
    }

    private void listarLivrosPorIdioma() {
        try {
            System.out.println("Digite o idioma: ");
            var idioma = scanner.nextLine();
            var json = consumo.obterDados(ENDERECO + "/books?languages=" + idioma);
            DadosLivro dados = converteDados.obterDados(json, DadosLivro.class);
            System.out.println(dados);
        } catch (Exception e) {
            System.err.println("Erro ao listar livros por idioma: " + e.getMessage());
        }
    }
}
