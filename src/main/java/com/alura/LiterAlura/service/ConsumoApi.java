package com.alura.LiterAlura.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConsumoApi {
    private static final Logger logger = LoggerFactory.getLogger(ConsumoApi.class);

    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            if (json == null || json.trim().isEmpty()) {
                logger.error("No data received from URL: {}", endereco);
                throw new RuntimeException("JSON data is empty or null");
            }
            return json;
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching data from URL: {}", endereco, e);
            throw new RuntimeException(e);
        }
    }
}
