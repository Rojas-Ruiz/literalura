package com.alura.literalura.model;

import com.alura.literalura.model.DTA.DatosIdiomas;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "idioma", schema = "public")
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String siglas;
    @OneToMany(mappedBy = "idioma", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Libro> libros;

    public Idioma() {}

    public Idioma(DatosIdiomas idioma) {
        this.siglas = idioma.idiomas().get(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(e -> e.setIdioma(this));
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Idioma:" + "\n    Codigo: " + siglas +
                "\n    Libros: " + libros;
    }
}
