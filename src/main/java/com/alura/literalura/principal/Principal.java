package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.model.DTA.*;
import com.alura.literalura.repository.*;
import com.alura.literalura.service.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Principal {
    //Instancias de conexion y conversion
    private Connection consumo = new Connection();
    private Conversion conversor = new Conversion();
    private  LibroRepository libroRepositorio;
    private  AutorRepository autoRepositorio;
    private IdiomaRepository idiomaRepositorio;
    private Scanner entrada = new Scanner(System.in);
    AtomicInteger count = new AtomicInteger(1);

    public Principal(LibroRepository libroRepositorio, AutorRepository autoRepositorio, IdiomaRepository idiomaRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autoRepositorio = autoRepositorio;
        this.idiomaRepositorio = idiomaRepositorio;
    }

    public void menu(){
        int opcion = 0;
        String menuMensaje = """
            Bienvenido al sistema de Libros Allura
            --------------------------------------
            1. Buscar libro por titulo
            2. Listar libros registrados
            3. Listar Autores registrados
            4. Listar Autores vivos en un determinado año
            5. Listar libros por idioma
            6. Top 10 libros mas descargados
            9. Salir
            """;

        while (opcion != 9) {
            System.out.println(menuMensaje);
            opcion = entrada.nextInt();
            switch (opcion) {
                case 1:
                    buscarPNombre();
                    break;
                case 2:
                    listarBuscados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    buscarPorFecha();
                    break;
                case 5:
                    listarPorIdioma();
                    break;
                case 6:
                    ranking10();
                    break;
                default:
                    break;
            }

        }
    }

    public void buscarPNombre(){
        entrada.nextLine();
        //Busqueda por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        String tituloL = entrada.nextLine();
        Optional<String> resultado = consumo.buscar(tituloL);
        if(resultado.isPresent() &&
                conversor.obtenerDatos(resultado.get(), Datos.class).resultados().size() > 0){

            Datos resultConsulta = conversor
                    .obtenerDatos(resultado.get(), Datos.class);
            DatosLibros resultLibro = resultConsulta.resultados().get(0);
            Autor resultAutor = new Autor(resultLibro.autor().isEmpty() ?
                    new DatosAutor("Desconocido", 0,0) :
                    resultLibro.autor().get(0));

            Libro libroG = new Libro(resultLibro);
            Idioma idiomaG = libroG.getIdioma();
            //consulta de exitencia en base de datos
            List<Autor> autorBase = autoRepositorio.findIdByNombreContainsIgnoreCase(resultAutor.getNombre());

            List<Libro> libroBase = libroRepositorio.findTituloByTituloContainsIgnoreCase(libroG.getTitulo());

            List<Idioma> idiomasBase = idiomaRepositorio.findBySiglas(idiomaG.getSiglas());

            //verificacion de idiomas en la base
            if (!idiomasBase.isEmpty()){
                idiomasBase.forEach(i -> {
                    if (idiomaG.getSiglas().contains(i.getSiglas())){
                        libroG.setIdioma(i);
                    } else {
                        idiomaRepositorio.save(idiomaG);
                    }
                });
            } else idiomaRepositorio.save(idiomaG);

            //verificacion de autores en la base
            if (autorBase.size() == 0 && libroBase.size() == 0){
                autoRepositorio.save(resultAutor);
                libroG.setAutor(resultAutor);
                libroRepositorio.save(libroG);
            } else if (autorBase.size() != 0 && libroBase.size() == 0){
                libroG.setAutor(autorBase.get(0));
                libroRepositorio.save(libroG);
            }
            System.out.println(libroG);
        } else {
            System.out.println("No se encontro el libro que buscas");
        }

    }

    public void listarBuscados(){
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.size() > 0){
            System.out.println("Los libros buscados son: ");
            libros.forEach(System.out::println);
        } else {
            System.out.println("No hay ningun libro guardado");
        }
    }

    private void listarAutores() {
        String menu = """
                1. Buscar autor por nombre
                2. Listar todos los autores
                9. Volver al inicio
                """;
        int opcion = 0;
        List<Autor> autores;
        while (opcion != 9){
            System.out.println(menu);
            opcion = entrada.nextInt();

            switch (opcion){
                case 1:
                    entrada.nextLine();
                    System.out.println("Ingrese el nombre del autor:");
                    String nombre = entrada.nextLine();
                    autores = autoRepositorio.findIdByNombreContainsIgnoreCase(nombre);
                    if (autores.size() > 0){
                        System.out.println("El autor buscado es:");
                        autores.forEach(autor -> System.out.println(autor.toString()));
                    } else {
                        System.out.println("No se encontro el autor");
                    }
                    break;
                case 2:
                    autores = autoRepositorio.findAll();
                    if (autores.size() > 0){
                        System.out.println("Los autores buscados son: ");
                        autores.forEach(System.out::println);
                    } else {
                        System.out.println("No hay autores registrados");
                    }
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        }
    }

    private void listarPorIdioma() {
        entrada.nextLine();

        System.out.println("--------------------------");
        System.out.println("Siglas de diomas guardados:");
        List<Idioma> idiomas = idiomaRepositorio.findAll();
        idiomas.forEach(i -> System.out.println(count+". "+i.getSiglas()));
        System.out.println("Introduzca una de las siglas:");
        String siglas = entrada.nextLine();
        idiomas.forEach(idioma -> {
            if (idioma.getSiglas().equalsIgnoreCase(siglas)){
                idioma.getLibros().stream().forEach(libro -> System.out.println(libro+"\n"));
            }
        });
    }

    private void buscarPorFecha() {
        String menu = """
                1. Buscar autores hasta un año limite
                2. Buscar autores desde un año hasta otro
                9. Volver al inicio
                """;
        int opcion = 0;
        int limite = 0;
        List<Autor> autores;
        while (opcion != 9){
            System.out.println(menu);
            opcion = entrada.nextInt();
            switch (opcion){
                case 1:
                    System.out.println("Ingrese el año limite hasta el que desea buscar:");
                    limite = entrada.nextInt();
                    autores = autoRepositorio.findByFechaFallecimientoLessThanEqual(limite);
                    if (autores.size() != 0){
                        autores.forEach(System.out::println);
                    } else  {
                        System.out.println("No se encontro ningun autor");
                    }
                    break;
                case 2:
                    System.out.println("Ingrese el año de nacimiento:");
                    int nacimiento = entrada.nextInt();
                    System.out.println("Ingrese el año hasta el que desea buscar:");
                    limite = entrada.nextInt();
                    autores = autoRepositorio.findByFechaNacimientoBetween(nacimiento, limite);
                    if (autores.size() != 0){
                        autores.forEach(System.out::println);
                    } else {
                        System.out.println("No se encontro ningun autor");
                    }
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        }
    }

    public void ranking10(){
        //Top 10 mas descargados
        System.out.println("Top 10 de los libros mas descargados: ");
        Optional<String> json = consumo.obtener();
        var datos = conversor.obtenerDatos(json.orElse(null), Datos.class);
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(l -> System.out.println((count.getAndIncrement())+". "+l));
    }
}
