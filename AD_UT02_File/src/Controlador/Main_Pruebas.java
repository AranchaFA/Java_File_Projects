package Controlador;

import DAOs.DAO_ByteFiles;
import FileFilters.*;
import Modelo.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_Pruebas {

    public static void main(String[] args) {
        
        // TODOS LOS MÉTODOS SE REFUNDIERON EN LA CLASE OperacionesFicheros, si se aplican desde
        // otra clase y da error, sólo habría que cambiar la clase que ejecuta el método estático

        // PRUEBAS EJERCICIO A) SENCILLO
        /*List<File> listaFicheros = null;
        String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros";
        try {
        listaFicheros = OperacionesFicheros.listarFicherosSencillo(ruta, true, true);
        Vista.Vista.mostrarListaDeFicheros(listaFicheros);
        } catch (MyExceptions.CarpetaVacia ex) {
        System.err.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MyExceptions.NoEsDirectorio ex) {
        System.err.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n\n");*/
        // PRUEBA EJERCICIO A CON RECURSIVIDAD
        /*String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros";
        try {
        Map<String, List> mapFicherosRecursivo = OperacionesFicheros.listarFicherosRecursivo(ruta);
        Vista.Vista.mostrarMapDeFicheros(mapFicherosRecursivo);
        System.out.println(mapFicherosRecursivo.size());
        } catch (MyExceptions.NoEsDirectorio ex) {
        System.err.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MyExceptions.CarpetaVacia ex) {
        System.err.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        /* // PRUEBAS EJERCICIO B)
        ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros";
        List<String> listaNombres = new ArrayList<>();
        int totalDirectoriosCreados = 0;
        listaNombres.add("1234");
        listaNombres.add("5678");
        listaNombres.add("aaaa"); // Ya existe
        try {
        totalDirectoriosCreados = OperacionesFicheros.crearDirectorios(ruta, listaNombres);
        } catch (MyExceptions.RutaIncorrecta ex) {
        System.err.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Total directorios creados : " + totalDirectoriosCreados);*/
        // PRUEBA EJERCICIO C)
        /*String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\directorio1";
        System.out.println(OperacionesFicheros.cambiarExtension(ruta, "jpg", "png")+" ficheros modificados.");*/
        // PRUEBA EJERCICIO 3 (Filtros)
        /*String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\directorio1";
        System.out.println("Filtrado por directorios");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileDirectorio()));
        System.out.println("Filtrado por imágenes");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileImagen()));
        System.out.println("Filtrado por vídeos");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileVideo()));
        System.out.println("Filtrado por tamaño mínimo 1KB");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileTamanhoMin(1000)));
        System.out.println("Filtrado por modificados en las últimas 24h");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileUltimaModificacion(24*60*60)));
        System.out.println("Filtrado por extensión (mkv)");
        Vista.Vista.mostrarListaDeFicheros(OperacionesFicheros.listarFicherosFiltrados(ruta, new FiltroFileExtension("mkv")));
         */
        /*// PRUEBA CIFRADO A) y B)
        // PARA LOS DOCUMENTOS INDIVIDUALES, HAY QUE METER LA RUTA CON LA EXTENSIÓN, SI NO NO LO PILLA
        String ruta3 = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\documento_txt.txt";
        new DAO_ByteFiles(ruta3).cifrarDescifrarFile(false);
        String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros";
        new DAO_ByteFiles(ruta).cifrarDescifrarSubficherosSelectivo(false, new FiltroFileExtension("txt"));*/
        // PRUEBA CONTAR LETRAS C)
        /*  String ruta3 = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\documento_txt.txt";
        try {
        Map<String, Integer> densidadLetras = new DAO_ByteFiles(ruta3).densidadLetras();
        Vista.Vista.mostrarMapDensidadDeLetras(densidadLetras);
        } catch (MyExceptions.RutaIncorrecta ex) {
        System.out.println(ex.getMessage());
        //Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        /*String ruta = "C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\documento_txt.txt";
        try {
        Vista.Vista.mostrarMapDensidadDeLetras(OperacionesFicheros.densidadLetras(ruta));
        } catch (MyExceptions.RutaIncorrecta ex) {
        System.out.println(ex.getMessage());
        // Logger.getLogger(Main_Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        // BUSCAR EL QUIJOTE
        /*List<File> listaQuijote = OperacionesFicheros.buscarFicheroRecursivo("quijote2", "C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios");
        Vista.Vista.mostrarListaDeFicheros(listaQuijote);
        File fileQuijote=listaQuijote.get(0);
        int numLineasQuijote = OperacionesReader.contarLineas(fileQuijote);
        System.out.println("El Quijote tiene "+numLineasQuijote+" líneas.");
        int numOcurrenciasPalabraQuijote=OperacionesReader.contarOcurrenciasDeUnaPalabra(fileQuijote, "Quijote");
        System.out.println("La palabra \"Quijote\" aparece "+numOcurrenciasPalabraQuijote+" veces.");
        System.out.println(OperacionesReader.contarLetras(fileQuijote));
        System.out.println(OperacionesReader.invertirLetras("Hola"));
        OperacionesReader.invertirLetrasDeLineas(fileQuijote);*/
        //OperacionesFicheros.limpiarSignosPuntuacion(new File("C:\\Users\\aranx\\OneDrive\\Escritorio\\ad_ficheros\\textoPuntuado.txt"));
        
        
    }
}
