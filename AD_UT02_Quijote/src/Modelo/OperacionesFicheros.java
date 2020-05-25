package Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contiene clases static para manejo de ficheros
 *
 * @author aranx
 */
public class OperacionesFicheros {

    /**
     * Map con todos los directorios y subdirectorios obtenidos, la ruta de
     * origen de los File será la key en el map de la lista a la que pertenecen.
     * List con todos los File que coinciden con el nombre buscado Hay que
     * inicializarlos aquí como atributos, porque si lo meto en los métodos
     * recursivos me los pisa cada vez que se ejecuta !!
     */
    public static Map<String, List> mapFicheros = new HashMap<>(); // ListarFicherosRecursivo
    public static List<File> listaFicheros = new ArrayList<>(); // BuscarFichero

    public static List<File> buscarFicheroRecursivo(String nombre, String ruta) {
        // Si la ruta está vacía, buscamos en el directorio raíz
        if (ruta.isEmpty()) {
            ruta = obtenerRutaDirectorioRaizPrincipal();
        }
        File ficheroRuta = new File(ruta);
        // Recorremos los ficheros hijos del directorio ruta buscando los ficheros cuyo nombre coincide con el buscado
        File[] arraySubficheros = ficheroRuta.listFiles();
        if (arraySubficheros != null) {
            for (File fichero : arraySubficheros) {
                if (OperacionesFicheros.getNombreSinExtension(fichero).equalsIgnoreCase(nombre) || fichero.getName().equalsIgnoreCase(nombre)) {
                    listaFicheros.add(fichero);
                }
                if (fichero.isDirectory()) {
                    try {
                        buscarFicheroRecursivo(nombre, fichero.getCanonicalPath());
                    } catch (IOException ex) {
                        System.out.println("Error al sacar canonical path");
                        Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return listaFicheros;
    }

    /**
     *
     * @return String con la ruta del directorio raíz del SO
     */
    public static String obtenerRutaDirectorioRaizPrincipal() {
        // Sacamos todos los ficheros raíz del SO
        File[] directoriosRaiz = File.listRoots();
        // Cogemos sólo el primer directorio raíz para listar sus directorios hijos 
        // (para no complicarlo con múltiples dispositivos de almacenaje)
        File directorioRaiz0 = directoriosRaiz[0];
        // Sacamos la ruta del directorio raíz
        String rutaDirectorioRaiz0 = null;
        try {
            rutaDirectorioRaiz0 = directorioRaiz0.getCanonicalPath();
        } catch (IOException ex) {
            System.err.println("Error al obtener la ruta del directorio raíz.");
            Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rutaDirectorioRaiz0;
    }

    /**
     *
     * @param fichero
     * @return El nombre del fichero, sin extensión
     */
    public static String getNombreSinExtension(File fichero) {
        // StringTokenizer para sacar el nombre en el primer token (hasta el '.')
        StringTokenizer stringTokenizer = new StringTokenizer(fichero.getName(), ".");
        return stringTokenizer.nextToken();
    }

    /**
     *
     * @param fichero
     * @return La canonicalPath sin la extensión del tipo de fichero
     */
    public static String getCanonicalPathSinExtension(File fichero) {
        // No sé si en algún SO me puedo encontrar '.' antes de la extensión en la canonicalPath, supongo que no
        // pero por si acaso saro ruta del fichero padre y nombre del fichero por separado
        String nombreDirectorioPadre = fichero.getParent();
        // StringTokenizer para sacar el nombre en el primer token (hasta el '.')
        StringTokenizer stringTokenizer = new StringTokenizer(fichero.getName(), ".");
        String nombreFicheroSinExtension = stringTokenizer.nextToken();
        return nombreDirectorioPadre + File.separator + nombreFicheroSinExtension;
    }

    /**
     *
     * @param fichero
     * @return La extensión del fichero (sin ".") pasado por parámetro, null si
     * se trata de un directorio y no tiene ninguna extensión
     */
    public static String getExtension(File fichero) {
        String extension = null;
        // StringTokenizer para sacar la extensión en el segundo token (después del '.')
        StringTokenizer stringTokenizer = new StringTokenizer(fichero.getName(), ".");
        stringTokenizer.nextToken();
        if (stringTokenizer.hasMoreTokens()) {
            extension = stringTokenizer.nextToken();
        }
        return extension;
    }

    public static String nombreFicheroModificado(File file, String apendiceModificacion) {
        return getCanonicalPathSinExtension(file) + apendiceModificacion + "." + getExtension(file);
    }

    // Deja al final un punto '.' y me quita los caracteres con tilde 0.o
    public static File limpiarSignosPuntuacion(File fichero) throws FileNotFoundException, IOException {
        // Guardamos en una lista los enteros ASCII de todos los caracteres a MANTENER (letras mayúsculas y minúsculas)
        // Para el guión (45), si va seguido de una letra será una palabra compuesta y se sustituye por un espacio (32)
        // si no, será una palabra partida a mitad de línea y se suprime
        List<Integer> listaLetras = SignosCastellanos.caracteresCastellanosASCII();
        // Añadimos el espacio en blanco
        listaLetras.add(SignosCastellanos.espacioEnBlancoASCII());
        // Recorremos el fichero byte a byte borrando los que no sean letras, EXCEPTO EXPACIOS,
        // Los guiones de palabras compuestas y saltos de línea se sustituirán por espacios !!
        // 13->Retorno de carro
        // 32->Espacio
        // 45->Guión
        File ficheroLimpio = new File(OperacionesFicheros.nombreFicheroModificado(fichero, "SinSignosPuntuacion"));

        FileInputStream fis = new FileInputStream(fichero);
        FileOutputStream fos = new FileOutputStream(ficheroLimpio);

        int caracterLeido = fis.read();
        int caracterAGrabar = 0;
        while (caracterLeido != -1) {
            // Si el caracter leído no es una letra
            if (!listaLetras.contains(caracterLeido)) {
                // Si es salto de carro lo grabamos
                if (caracterLeido == 13) {
                    //caracterAGrabar = 32;
                    fos.write(caracterLeido);
                }
                // Si es guión comprobamos el siguiente caracter
                if (caracterLeido == 45) {
                    caracterLeido = fis.read();
                    // Si no es salto de carro, grabamos un espacio y el último caracter leído (el siguiente al guión)
                    if (caracterLeido != 13) {
                        caracterAGrabar = 32;
                        fos.write(caracterAGrabar);
                        fos.write(caracterLeido);
                    } else {
                        // Si no es salto de carro, grabamos sólo el último caracter leído (el siguiente al guión)
                        // para que nos quede 'pegado' a la anterior letra y así se una la palabra
                        fos.write(caracterLeido);
                    }
                }
            } else {
                // Si está en la lista de letras, grabamos el caracter leído
                fos.write(caracterLeido);
            }
            // Leemos un nuevo caracter a comprobar
            caracterLeido = fis.read();
        }
        fos.close();
        fis.close();
        return ficheroLimpio;
    }

    // NO FUNCIONA! Da una excepción por regex :(
    public static String limpiarSignosPuntuacion(String string) {
        String stringLimpio = string;
        List<Integer> signosPuntuacion = SignosCastellanos.signosPuntuacionSinEspacioASCII();
        // Borramos todos los signos menos el espacio en blanco
        // No tengo en cuenta los guiones que parten palabras entre líneas
        for (Integer integer : signosPuntuacion) {
            char caracterChar = (char) integer.intValue();
            String caracterString = String.valueOf(caracterChar);
            stringLimpio = stringLimpio.replaceAll(caracterString, "");
        }
        return stringLimpio;
    }
}
