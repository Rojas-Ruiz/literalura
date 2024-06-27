package com.alura.literalura.model.DTA;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DatosIdiomas(
        @JsonAlias("lenguajes") List<String> idiomas
) {
}
