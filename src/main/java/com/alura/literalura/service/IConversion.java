package com.alura.literalura.service;

public interface IConversion {
    <T> T obtenerDatos(String json, Class<T> clase);
}



