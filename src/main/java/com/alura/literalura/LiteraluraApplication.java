package com.alura.literalura;

import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.IdiomaRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.alura.literalura.principal.Principal;

// Comienzo y ejecucion del programa
@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	LibroRepository libroRepositorio;
	@Autowired
	AutorRepository autoRepositorio;
	@Autowired
	IdiomaRepository idiomaRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepositorio, autoRepositorio, idiomaRepositorio);
		principal.menu();
	}
}
