package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class OperacionesBufferedFiles {

    public static int contarLineas(File file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        long numLineasLong = br.lines().count();

        br.close();
        fr.close();
        return (int) numLineasLong;
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

    public static Map<File,Map<String, Integer>> densidadPalabrasSubficheros(File directorio) throws IOException{
        Map<File,Map<String, Integer>> map=new HashMap<>();
        // Procesamos sólo los ficheros hijos del directorio, podríamos hacerlo recursivo
        File[] filesTxt = directorio.listFiles(new FileFilters.FiltroFileExtension("txt"));
        
        for (File file : filesTxt) {
            map.put(file, densidadPalabras(file));
        }
        
        return map;
    }
}
