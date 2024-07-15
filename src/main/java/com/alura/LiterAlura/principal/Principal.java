package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.Autor;
import com.alura.LiterAlura.model.DadosAutor;
import com.alura.LiterAlura.model.DadosLivro;
import com.alura.LiterAlura.model.Livro;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LivroRepository;
import com.alura.LiterAlura.service.ConsumoApi;
import com.alura.LiterAlura.service.ConverteDados;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private static final Logger logger = LoggerFactory.getLogger(Principal.class);
    private static final String ENDERECO = "https://gutendex.com";

    private final Scanner scanner;
    private final ConsumoApi consumo;
    private final ConverteDados converteDados;
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    @Autowired
    public Principal(LivroRepository livroRepository, AutorRepository autorRepository, ConsumoApi consumo, ConverteDados converteDados) {
        this.scanner = new Scanner(System.in);
        this.consumo = consumo;
        this.converteDados = converteDados;
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibirMenu() {
        try {
            while (true) {
                System.out.println("Escolha uma opção: \n");
                System.out.println("1 - Buscar livro pelo título");
                System.out.println("2 - Listar livros registrados");
                System.out.println("3 - Buscar autores registrados");
                System.out.println("4 - Listar autores vivos em determinado ano");
                System.out.println("5 - Listar livros em determinado idioma");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                int escolha = Integer.parseInt(scanner.nextLine());

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
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
        } finally {
            scanner.close();
        }
    }

    private void listaLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void buscarLivro() {
        try {
            System.out.print("Digite o nome do livro: ");
            var titulo = scanner.nextLine();
            var json = consumo.obterDados(ENDERECO + "/books?search=" + titulo);
            DadosLivro dadosLivro = converteDados.obterDados(json, DadosLivro.class);

            if (dadosLivro == null || dadosLivro.titulo() == null) {
                System.out.println("Livro não encontrado.");
                return;
            }

            System.out.println(dadosLivro);

            if (!livroRepository.existsByTitulo(dadosLivro.titulo())) {
                Livro livro = new Livro(dadosLivro);

                // Set autores
                for (DadosAutor dadosAutor : dadosLivro.autores()) {
                    Autor autor = new Autor(dadosAutor);
                    autor.setLivro(livro);
                    livro.addAutor(autor);
                }

                // Set idiomas
                livro.setIdiomas(dadosLivro.idiomas());

                livroRepository.save(livro);
                System.out.println("Livro salvo: " + livro.getTitulo());
            } else {
                System.out.println("Livro já existe no banco de dados.");
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar livro: {}", e.getMessage(), e);
            System.out.println("Erro ao buscar livro. Tente novamente mais tarde.");
        }
    }




    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void buscarAutoresVivosPorAno() {
        try {
            System.out.print("Digite o ano a ser buscado: ");
            String ano = scanner.nextLine();
            String json = consumo.obterDados(ENDERECO + "/books?author_year_start=" + ano);
            List<DadosLivro> dadosLivros = converteDados.obterDados(json, new TypeReference<List<DadosLivro>>() {});

            if (dadosLivros == null || dadosLivros.isEmpty()) {
                System.out.println("Nenhum autor encontrado.");
                return;
            }

            dadosLivros.forEach(livro -> livro.autores().forEach(autor -> {
                if (autor.anoFalecimento() == null || autor.anoFalecimento() > Integer.parseInt(ano)) {
                    System.out.println(autor.nome());
                }
            }));
        } catch (Exception e) {
            logger.error("Erro ao buscar autores por ano: {}", e.getMessage(), e);
            System.out.println("Erro ao buscar autores por ano. Tente novamente mais tarde.");
        }
    }


    private void listarLivrosPorIdioma() {
        try {
            System.out.print("Digite o idioma: ");
            String idioma = scanner.nextLine();
            String json = consumo.obterDados(ENDERECO + "/books?languages=" + idioma);
            List<DadosLivro> dadosLivros = converteDados.obterDados(json, new TypeReference<List<DadosLivro>>() {});

            if (dadosLivros == null || dadosLivros.isEmpty()) {
                System.out.println("Nenhum livro encontrado.");
                return;
            }

            dadosLivros.forEach(livro -> System.out.println(livro.titulo()));
        } catch (Exception e) {
            logger.error("Erro ao listar livros por idioma: {}", e.getMessage(), e);
            System.out.println("Erro ao listar livros por idioma. Tente novamente mais tarde.");
        }
    }

    }

