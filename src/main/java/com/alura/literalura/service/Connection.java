package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class Connection {
    private HttpClient client;
    private String url;
    private HttpRequest request;
    HttpResponse<String> response;

    public Connection() {
        client = HttpClient.newHttpClient();
        url = "https://gutendex.com/books/";
    }

    public Optional<String> obtener(){
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        response = enviaConsulta(request);
        return Optional.ofNullable(response.body());
    }

    private HttpResponse<String> enviaConsulta(HttpRequest request) {
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Optional<String> buscar(String titulo){
        request = HttpRequest.newBuilder()
                .uri(URI.create(url+"?search="+titulo.replace(" ", "+")))
                .build();
        response = enviaConsulta(request);
        return Optional.ofNullable(response.body());
    }
}
