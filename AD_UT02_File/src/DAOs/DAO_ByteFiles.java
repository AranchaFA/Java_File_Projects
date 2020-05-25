package DAOs;

import Modelo.MyExceptions;
import Modelo.OperacionesFicheros;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAO_ByteFiles {

// ATRIBUTOS
    private File ficheroDeBytes;
    // Para leer el archivo
    private FileInputStream fis;
    // Para grabar el archivo
    private FileOutputStream fos;

    // MÉTODOS
    // Getters + Setters
    public File getFicheroDeBytes() {
        return ficheroDeBytes;
    }

    public void setFicheroDeBytes(File ficheroDatosPrimitivos) {
        this.ficheroDeBytes = ficheroDatosPrimitivos;
    }

    /* CONSTRUCTORES:
    1.- Metiendo el OBJETO File ya creado.
    2.- Metiendo el NOMBRE del fichero para crear el objeto file.
    (Para leer debe existir el archivo, para grabar no obligatoriamente).
     */
    public DAO_ByteFiles(File ficheroDatosPrimitivos) {
        this.ficheroDeBytes = ficheroDatosPrimitivos;
    }

    public DAO_ByteFiles(String nombreFichero) {
        this.ficheroDeBytes = new File(nombreFichero);
    }

    // PARA LEER
    public void abrirLeer() throws FileNotFoundException {
        fis = new FileInputStream(ficheroDeBytes);
    }

    public void cerrarLeer() throws IOException {
        fis.close();
    }

    // Método para leer un byte, devuelve un int que será su valor en ascii
    public int leerUnRegistro() throws IOException {
        int enteroASCII = fis.read();
        return enteroASCII;
    }

    // PARA GRABAR 
    // Añadiendo al final de un fichero existente
    public void abrirGrabarAnhadir() throws FileNotFoundException {
        fos = new FileOutputStream(ficheroDeBytes, true);
    }

    // Sobreescribiendo un fichero existente
    public void abrirGrabarSobreescribir() throws FileNotFoundException {
        fos = new FileOutputStream(ficheroDeBytes);
    }

    public void cerrarGrabar() throws IOException {
        fos.close();
    }

    // Método para grabar un byte
    public void grabarUnRegistro(int enteroASCII) throws IOException {
        fos.write(enteroASCII);
    }

    // CIFRADO Y DESCIFRADO César
    // (cada caracter se sustituye por el que está 3 posiciones por delante : A->D,B->E,...)
    // Hay que pasar la ruta del fichero con extensión, si no casca
    // Parámetro true->Fichero cifrado, lo descifrará; false->Fichero descifrado, lo cifrará
    public File cifrarDescifrarFile(boolean cifrado) {
        if (ficheroDeBytes.isDirectory()) {
            return null;
        }
        // Creamos un DAO para el fichero cifrado de salida
        // Obtenemos el nombre para el nuevo fichero cifrado/descifrado que se creará a partir del nombre del fichero original (nombre.extension->nombreCifrado.extension)
        // No me convencen los métodos para hacerlo con la clase String, me fío más de sacarlo con los métodos de la clase File aunque ocupe mucho
        String rutaFileResultante;
        if (cifrado) {
            rutaFileResultante = OperacionesFicheros.getCanonicalPathSinExtension(ficheroDeBytes) + "Descifrado." + OperacionesFicheros.getExtension(ficheroDeBytes);
        } else {
            rutaFileResultante = OperacionesFicheros.getCanonicalPathSinExtension(ficheroDeBytes) + "Cifrado." + OperacionesFicheros.getExtension(ficheroDeBytes);
        }
        DAO_ByteFiles daoFileResultante = new DAO_ByteFiles(new File(rutaFileResultante));
        try {
            // Abrimos flujos
            this.abrirLeer();
            daoFileResultante.abrirGrabarSobreescribir();
            // Leemos hasta que nos devuelva un (-1)->Fin de fichero
            // y grabamos el valor ascii leído incrementado en 3
            int asciiLeido = 0;
            do {
                asciiLeido = this.leerUnRegistro();
                int asciiAGrabar;
                if (cifrado) {
                    asciiAGrabar = asciiLeido - 3;
                    // Haya 255 signos ASCII, a partir del 253 hay que volver a empezar en el 0
                    if (asciiAGrabar < 0) {
                        asciiAGrabar += 256; // Debe empezar de nuevo en el 0, con 255 empezaría en el 1
                    }
                } else {
                    asciiAGrabar = asciiLeido + 3;
                    // Haya 255 signos ASCII, a partir del 253 hay que volver a empezar en el 0
                    if (asciiAGrabar > 255) {
                        asciiAGrabar -= 256; // Debe empezar de nuevo en el 0, con 255 empezaría en el 1
                    }
                }
                daoFileResultante.grabarUnRegistro(asciiAGrabar);
            } while (asciiLeido != -1);
            // Cerramos flujos
            this.cerrarLeer();
            daoFileResultante.cerrarGrabar();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error al leer/grabar fichero.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daoFileResultante.getFicheroDeBytes();
    }

    // Cifrar todos los documentos de un determinado tipo contenidos en el directorio del File del DAO (deberíamos comprobar que es directorio)
    public int cifrarDescifrarSubficherosSelectivo(boolean cifrado, FileFilter filtro) {
        int ficherosTratados = 0;
        List<File> listaFicheros = Arrays.asList(ficheroDeBytes.listFiles(filtro));
        for (File fichero : listaFicheros) {
            // Creamos un DAO para el fichero leído y lo ciframos/desciframos
            new DAO_ByteFiles(fichero).cifrarDescifrarFile(cifrado);
            ficherosTratados++;
        }
        return ficherosTratados;
    }

    public Map<String, Integer> densidadLetras() throws MyExceptions.RutaIncorrecta {
         if (!ficheroDeBytes.exists()) {
            throw new MyExceptions.RutaIncorrecta("La ruta no existe.");
        }
        if (OperacionesFicheros.getExtension(ficheroDeBytes)==null||!OperacionesFicheros.getExtension(ficheroDeBytes).equalsIgnoreCase("txt")) {
        throw new MyExceptions.RutaIncorrecta("La ruta no coreesponde a un fichero de texto.");
        }
        Map<String, Integer> mapDensidadLetras = new HashMap<>();
        try {
            // Abrimos flujo
            abrirLeer();
            // Leemos hasta que nos devuelva un (-1)->Fin de fichero
            // y lo registramos en el map: si no existe lo registrsamos con valor 1, si existe sumamos 1
            int asciiLeido = 0;
            do {
                asciiLeido = leerUnRegistro();
                String caracter = String.valueOf((char) asciiLeido);
                // Integer repeticiones=mapDensidadLetras.get(caracter);
                if (mapDensidadLetras.containsKey(caracter)) {
                    int valorActual = mapDensidadLetras.get(caracter);
                    mapDensidadLetras.put(caracter, valorActual + 1);
                } else {
                    mapDensidadLetras.put(caracter, 1);
                }
                // mapDensidadLetras.put(caracter, (repeticiones==null)?1:repeticiones+1);
            } while (asciiLeido != -1);
            // Cerramos flujo
            cerrarLeer();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error al leer/grabar fichero.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapDensidadLetras;
    }


}
