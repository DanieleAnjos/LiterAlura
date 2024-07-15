package com.alura.LiterAlura.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConverteDados {
    private final ObjectMapper objectMapper;

    public ConverteDados(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T obterDados(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados", e);
        }
    }

    public <T> List<T> obterDados(String json, TypeReference<List<T>> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados", e);
        }
    }
}
