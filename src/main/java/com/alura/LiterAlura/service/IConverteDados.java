package com.alura.LiterAlura.service;

public interface IConverteDados {
    <L> L obterDados(String json, Class<L> classe);

}
