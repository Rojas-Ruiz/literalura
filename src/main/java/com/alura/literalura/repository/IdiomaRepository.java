package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface IdiomaRepository extends JpaRepository<Idioma, Integer>{
    List<Idioma> findBySiglas(String siglas);

}
