package Modelo;

import DAOs.DAO_ByteFiles;
import FileFilters.*;
import Modelo.ComparatorsFiles.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    ////////////////////////////////
    // EJERCICIO A)
    ///////////////////////////////
    /**
     *
     * @param ruta del directorio origen
     * @param ordenadosPorTamanho true si se desean ordenados por tamaño
     * ascendente
     * @param soloDirectorios true si se desea listar sólo los ficheros que son
     * directorios
     * @return lista con los ficheros contenidos en el directorio de la ruta
     * (sólo ficheros hijos). Si la ruta está vacía, lista con los ficheros
     * contenidos en la raíz del SO.
     * @throws MyExceptions.NoEsDirectorio si la ruta del directorio origen no
     * corresponde a un directorio sino a otro tipo de fichero
     * @throws MyExceptions.CarpetaVacia si la ruta del directorio origen
     * corresponde a una carpeta vacía
     */
    public static List<File> listarFicherosSencillo(String ruta, boolean ordenadosPorTamanho, boolean soloDirectorios) throws MyExceptions.NoEsDirectorio, MyExceptions.CarpetaVacia {
        List<File> listaFicheros = null;
        // Si la ruta está vacía empleamos la ruta del directorio raíz
        if (ruta.isEmpty()) {
            ruta = obtenerRutaDirectorioRaizPrincipal();
        }
        File ficheroRuta = new File(ruta);
        // Si no es un directorio salta una excepción
        if (!ficheroRuta.isDirectory()) {
            throw new MyExceptions.NoEsDirectorio("No es directorio! No se puede listar.");
        }
        // Si el directorio está vacío salta una excepción
        if (ficheroRuta.list().length == 0) {
            throw new MyExceptions.CarpetaVacia("El directorio está vacío.");
        }
        // SÓLO DIRECTORIOS SÍ/NO 
        if (soloDirectorios) {
            listaFicheros = listarSoloDirectorios(ruta);
        } else {
            listaFicheros = Arrays.asList(new File(ruta).listFiles());
        }
        // ORDENADOS POR TAMAÑO SÍ/NO
        if (ordenadosPorTamanho) {
            listaFicheros.sort(new ComparatorFileTamanho());
        }
        return listaFicheros;
    }

    /**
     *
     * @param ruta
     * @return lista con todos los ficheros y subficheros contenidos en el
     * directorio de la ruta. Si la ruta está vacía, lista con los ficheros
     * contenidos en la raíz del SO.
     * @throws MyExceptions.NoEsDirectorio si la ruta del directorio origen no
     * corresponde a un directorio sino a otro tipo de fichero
     * @throws MyExceptions.CarpetaVacia si la ruta del directorio origen
     * corresponde a una carpeta vacía
     */
    public static Map<String, List> listarFicherosRecursivo(String ruta) throws MyExceptions.NoEsDirectorio, MyExceptions.CarpetaVacia {
        // SI CREAMOS AQUÍ EL MAP, LO PISA CADA VEZ QUE SE EJECUTA RECURSIVAMENTE DENTRO DEL BUCLE 
        // Y SÓLO NOS DEVUELVE LOS FICHEROS DEL PRIMER DIRECTORIO (RUTA DEL PARÁMETRO)
        // Map<String,List> mapFicheros= new HashMap<>();    

        // Si la ruta está vacía empleamos la ruta del directorio raíz
        if (ruta.isEmpty()) {
            ruta = obtenerRutaDirectorioRaizPrincipal();
        }
        File ficheroRuta = new File(ruta);
        // Listamos los ficheros de la ruta y los añadimos al map, usando como clave la ruta del fichero
        List<File> listaSubFicheros = Arrays.asList(ficheroRuta.listFiles());
        try {
            mapFicheros.put(ficheroRuta.getCanonicalPath(), listaSubFicheros);
        } catch (IOException ex) {
            new MyExceptions.RutaIncorrecta("Se ha encontrado una ruta incorrecta: " + ruta);
            // Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Recorremos los sub-ficheros de la ruta, y si es directorio a su vez listamos sus sub-ficheros
        for (File fichero : listaSubFicheros) {
            if (fichero.isDirectory()) {
                try {
                    listarFicherosRecursivo(fichero.getCanonicalPath());
                } catch (IOException ex) {
                    new MyExceptions.RutaIncorrecta("Se ha encontrado una ruta incorrecta: " + fichero.getAbsolutePath());
                    // Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return mapFicheros;
    }

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
     * @param listaDeFicheros
     * @return lista de ficheros pasada por parámetro ordenada por tamaño
     * ascendente
     */
    public static List<File> ordenarListaFicherosPorTamanho(List<File> listaDeFicheros) {
        listaDeFicheros.sort(new ComparatorsFiles.ComparatorFileTamanho());
        return listaDeFicheros;
    }

    /**
     *
     * @param ruta del directorio origen
     * @return lista con los subdirectorios del directorio de la ruta de origen
     */
    public static List<File> listarSoloDirectorios(String ruta) {
        return Arrays.asList(new File(ruta).listFiles(new FiltroFileDirectorio()));
    }

    ////////////////////////////////
    // EJERCICIO B)
    ///////////////////////////////
    /**
     * Crea en la ruta de origen los nuevos directorios cuyos nombres pasamos en
     * una lista. Salta una excepción en caso de que un directorio a crear ya
     * exista.
     *
     * @param rutaOrigen en la que se quieren crear los directorios
     * @param listaNombresDirectorios lista con los nombres a asignar a los
     * subdirectorios
     * @return número de directorios nuevos que se han creado
     * @throws MyExceptions.RutaIncorrecta si la ruta de origen no existe
     */
    public static int crearDirectorios(String rutaOrigen, List<String> listaNombresDirectorios) throws MyExceptions.RutaIncorrecta {
        int numDirectoriosCreados = 0;
        File directorioOrigen = new File(rutaOrigen);
        if (!directorioOrigen.exists()) {
            throw new MyExceptions.RutaIncorrecta("La ruta de origen no existe.");
        }

        for (String nombreDirectorio : listaNombresDirectorios) {
            try {
                if (crearUnDirectorio(rutaOrigen, nombreDirectorio)) {
                    numDirectoriosCreados++;
                }
            } catch (MyExceptions.DirectorioYaExiste ex) {
                System.err.println(ex.getMessage());
                //Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return numDirectoriosCreados;
    }

    /**
     * Crea en la ruta de origen un nuevo directorio con el nombre pasado por
     * parámetro. Salta una excepción en caso de que el directorio a crear ya
     * exista.
     *
     * @param rutaOrigen en la cual se quiere crear el directorio
     * @param nombre
     * @return
     * @throws MyExceptions.DirectorioYaExiste
     */
    public static boolean crearUnDirectorio(String rutaOrigen, String nombre) throws MyExceptions.DirectorioYaExiste {
        // Con File.pathSeparator pone ; en lugar de \, y los crea como hermanos de la ruta origen y no dentro de ella
        File directorioACrear = new File(rutaOrigen + File.separator + nombre);
        if (directorioACrear.exists()) {
            throw new MyExceptions.DirectorioYaExiste("El directorio que intentas crear ya existe : " + directorioACrear.getPath());
        } else {
            directorioACrear.mkdir();
            return true;
        }
    }

    ////////////////////////////////
    // EJERCICIO C)
    ///////////////////////////////
    /**
     * Cambia la extensión de un fichero
     *
     * @param rutaOrigen ruta del fichero cuya extensión se quiere cambiar
     * @param extensionAntigua Extensión del fichero original
     * @param extensionNueva Extensión que se quiere asignar
     * @return
     */
    public static int cambiarExtension(String rutaOrigen, String extensionAntigua, String extensionNueva) {
        int ficherosModificados = 0;
        File ficheroOrigen = new File(rutaOrigen);
        List<File> listaFicherosAModificar = Arrays.asList(ficheroOrigen.listFiles(new FiltroFileExtension(extensionAntigua)));
        for (File file : listaFicherosAModificar) {
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(file.getCanonicalPath(), ".");
                if (file.renameTo(new File(stringTokenizer.nextToken() + "." + extensionNueva))) {
                    ficherosModificados++;
                }
            } catch (IOException ex) {
                System.out.println("Ruta incorrecta.");
                Logger.getLogger(OperacionesFicheros.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ficherosModificados;
    }

    ////////////////////////////////
    // EJERCICIO 3)
    ///////////////////////////////
    /**
     * Devuelve una lista con los ficheros de un directorio que cumplan una
     * determinada condición
     *
     * @param rutaOrigen Ruta del directorio
     * @param fileFilter Filtro que deben pasar los ficheros para ser listados
     * @return
     */
    public static List<File> listarFicherosFiltrados(String rutaOrigen, FileFilter fileFilter) {
        return Arrays.asList(new File(rutaOrigen).listFiles(fileFilter));
    }

    ////////////////////////////////
    // EJERCICIO 4)
    ///////////////////////////////
    // Metodillos para mí :)
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
     * @return La extensión del fichero (sin ".") pasado por parámetro, null si se trata
     * de un directorio y no tiene ninguna extensión
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

    public static String nombreFicheroModificado(File file,String apendiceModificacion){
        return getCanonicalPathSinExtension(file)+apendiceModificacion+"."+getExtension(file);
    }
    
    public static Map<String, Integer> densidadLetras(String ruta) throws MyExceptions.RutaIncorrecta {
        File fichero = new File(ruta);
        if (!fichero.exists()) {
            throw new MyExceptions.RutaIncorrecta("La ruta no existe.");
        }
        if (OperacionesFicheros.getExtension(fichero) == null || !OperacionesFicheros.getExtension(fichero).equalsIgnoreCase("txt")) {
            throw new MyExceptions.RutaIncorrecta("La ruta no coreesponde a un fichero de texto.");
        }
        Map<String, Integer> mapDensidadLetras = new HashMap<>();
        try {
            // Abrimos flujo
            FileInputStream fis = new FileInputStream(fichero);
            // Leemos hasta que nos devuelva un (-1)->Fin de fichero
            // y lo registramos en el map: si no existe lo registramos con valor 1, si existe sumamos 1
            int asciiLeido = 0;
            do {
                asciiLeido = fis.read();
                String caracter = String.valueOf((char) asciiLeido);
                if (mapDensidadLetras.containsKey(caracter)) {
                    int valorActual = mapDensidadLetras.get(caracter);
                    mapDensidadLetras.put(caracter, valorActual + 1);
                } else {
                    mapDensidadLetras.put(caracter, 1);
                }
            } while (asciiLeido != -1);
            // Cerramos flujo
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error al leer/grabar fichero.");
            Logger.getLogger(DAO_ByteFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapDensidadLetras;
    }
    
    // Deja al final un punto '.' y me quita los caracteres con tilde, a pesar de que los tengo en cuenta 0.o
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

     public static long contarLineas(File file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        long numLineasLong = br.lines().count();

        br.close();
        fr.close();
        return numLineasLong;
    }

    /**
     * Cuenta el número de veces que aparece una palabra (independiente, no
     * admite coincidencias en una secuencia de caracteres contenida en una
     * palabra de mayor tamaño) en un String. Al aplicarse a un String, no tiene
     * en cuenta las palabras cortadas a final de línea por un guión
     *
     * @param texto
     * @param palabra
     * @return número de veces que se encuentra esa palabra en el texto
     */
    public static int ocurrenciasPalabra(String texto, String palabra) {
        int numOcurrencias = 0;
        texto = texto.toUpperCase();
        palabra = palabra.toUpperCase();

        while (texto.indexOf(palabra) > -1) {
            boolean independiente = true;
            if (texto.indexOf(palabra) > 0) { // Si la palabra no está al inicio de línea
                // Si el caracter anterior es una letra, no es palabra independiente
                String caracterAnterior = String.valueOf(texto.charAt(texto.indexOf(palabra) - 1));
                if (SignosCastellanos.caracteresCastellanos().contains(caracterAnterior)) {
                    independiente = false;
                }
            }
            if (texto.indexOf(palabra) + palabra.length() < texto.length()) { // Si la palabra no está al final de línea
                // Si el caracter posterior es una letra, no es palabra independiente
                String caracterPsterior = String.valueOf(texto.charAt(texto.indexOf(palabra) + palabra.length()));
                if (SignosCastellanos.caracteresCastellanos().contains(caracterPsterior)) {
                    independiente = false;
                }
            }
            if (independiente) {
                numOcurrencias++;
            }
            // Cogemos el último caracter de la palabra, porque si no lo puede contar como 
            // bueno al cortar la secuencia anterior si también coincidía
            texto = texto.substring(texto.indexOf(palabra) + palabra.length() - 1);
        }
        return numOcurrencias;
    }

    public static int ocurrenciasPalabra(File file, String palabra) throws FileNotFoundException, IOException {
        int numOcurrencias = 0;

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            numOcurrencias += ocurrenciasPalabra(lineaLeida, palabra);
            lineaLeida = br.readLine();
        }

        br.close();
        fr.close();

        return numOcurrencias;
    }

    // Al final no lo uso en este ejercicio, pero es práctico guardarlo
    public static int ocurrenciaDeUnCaracter(String linea, String caracter) {
        int numOcurrencias = 0;
        char caracterChar = caracter.charAt(0);
        for (int i = 0; i < linea.length(); i++) {
            if (linea.charAt(i) == caracterChar) {
                numOcurrencias++;
            }
        }
        return numOcurrencias;
    }
    
    public static int contarLetras(String linea) {
        int numLetras = 0;
        // Lista con los valores enteros de las letras en ASCII
        List<Integer> listaLetras = SignosCastellanos.caracteresCastellanosASCII();
        for (int i = 0; i < linea.length(); i++) {
            char caracterLeido = linea.charAt(i);
            if (listaLetras.contains((int) caracterLeido)) {
                numLetras++;
            }
        }
        return numLetras;
    }

    public static int contarLetras(File fichero) throws FileNotFoundException, IOException {
        int numLetras = 0;
        FileReader fr = new FileReader(fichero);
        BufferedReader br = new BufferedReader(fr);

        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            numLetras += contarLetras(lineaLeida);
            lineaLeida = br.readLine();
        }

        br.close();
        fr.close();

        return numLetras;
    }
    
    public static String revertirLetras(String string) {
        return new StringBuilder(string).reverse().toString();
    }

    public static File revertirLetrasDeLineas(File file) throws FileNotFoundException, IOException {
        File fileRevertido = new File(OperacionesFicheros.nombreFicheroModificado(file, "Revertido"));
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(fileRevertido);
        BufferedWriter bw = new BufferedWriter(fw);

        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            bw.write(revertirLetras(lineaLeida) + "\n"); // Línea con salto de línea al final
            lineaLeida = br.readLine();
        }

        br.close();
        fr.close();
        bw.close();
        fw.close();

        return fileRevertido;
    }

    // Da inconvenientes con los caracteres con tilde, no los pilla a pesar de que están guardados en la lista de caracteres castellanos 0.o
    public static Map<String, Integer> densidadPalabras(File file) throws FileNotFoundException, IOException {
        Map<String, Integer> mapDensidadPalabras = new HashMap<>();
        File ficheroSinSignosPuntuacion = OperacionesFicheros.limpiarSignosPuntuacion(file);

        FileReader fr = new FileReader(ficheroSinSignosPuntuacion);
        BufferedReader br = new BufferedReader(fr);

        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            // Tokenizamos la línea SIN SIGNOS DE PUNTUACIÓN usando el espacio como delimitador
            for (StringTokenizer st = new StringTokenizer(lineaLeida.toUpperCase(), " "); st.hasMoreTokens();) {
                String palabra = st.nextToken();
                if (!mapDensidadPalabras.containsKey(palabra.toUpperCase())) {
                    mapDensidadPalabras.put(palabra, 1);
                } else {
                    mapDensidadPalabras.put(palabra, mapDensidadPalabras.get(palabra) + 1);
                }
            }
            lineaLeida = br.readLine();
        }

        br.close();
        fr.close();

        ficheroSinSignosPuntuacion.delete(); // Borramos el fichero auxiliar sin signos de puntuación
        return mapDensidadPalabras;
    }

    public static Map<File,Map<String, Integer>> densidadPalabrasSubficheros(File directorio) throws IOException{
        Map<File,Map<String, Integer>> map=new HashMap<>();
        // Procesamos sólo los ficheros hijos del directorio, podríamos hacerlo recursivo
        File[] filesTxt = directorio.listFiles(new FileFilters.FiltroFileExtension("txt"));
        
        for (File file : filesTxt) {
            map.put(file, densidadPalabras(file));
        }
        
        return map;
    }
    
    // Corta en medio de algunos capítulos en que la palabra 'capítulo' aparece en el texto :)
    public static Map<Integer, File> desglosarCapitulos(File file) throws IOException {
        Map<Integer, File> mapCapitulos = new HashMap<>();
        int contadorCap = 0;
        String nombreFicheroCap = OperacionesFicheros.nombreFicheroModificado(file, "Cap" + contadorCap);
        File fileCap = new File(nombreFicheroCap); // El Cap0 guaerdaría el título, autor...

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(fileCap);
        BufferedWriter bw = new BufferedWriter(fw);
        // No cuadra bien la numeración de los ficheros, pero los crea
        String lineaLeida = br.readLine();
        while (lineaLeida != null) {
            if (lineaLeida.toUpperCase().contains("CAPÍTULO")) {
                // Cierro los flujos ya abiertos por si acaso
                bw.close();
                fw.close();
                mapCapitulos.put(contadorCap, fileCap);
                contadorCap++;
                nombreFicheroCap = OperacionesFicheros.nombreFicheroModificado(file, "Cap" + contadorCap);
                fileCap = new File(nombreFicheroCap);
                fw = new FileWriter(fileCap);
                bw = new BufferedWriter(fw);
                bw.write(lineaLeida);
                bw.newLine();
                bw.flush();
            } else {
                bw.write(lineaLeida);
                bw.newLine();
                bw.flush();
            }
            lineaLeida = br.readLine();
        }
        
        br.close();
        fr.close();
        bw.close();
        fw.close();

        return mapCapitulos;
    }

    
}
