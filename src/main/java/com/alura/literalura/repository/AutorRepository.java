package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    List<Autor> findIdByNombreContainsIgnoreCase(String nombre);
    List<Autor> findByFechaFallecimientoLessThanEqual(int fechalimite);
    List<Autor> findByFechaNacimientoBetween(int fechaNacimiento, int limite);
}
