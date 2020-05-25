package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OperacionesReader {

    public static int contarLineas(File fichero) {
        FileReader fr = null;
        int numLineas = 0;
        try {
            fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);
            // br.lines().count(); 
            String lineaLeida = br.readLine();
            while (lineaLeida != null) {
                numLineas++;
                lineaLeida = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numLineas;
    }

    public static int contarOcurrenciasDeUnaPalabra(File fichero, String palabra) {
        int numOcurrencias = 0;
        FileReader fr = null;
        String signoSustituto="+";
        try {
            fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);
            // Reemplazo la palabra buscada por el signo "+", y luego cuento cuántas veces aparece en esa línea
            String lineaLeida = br.readLine();
            while (lineaLeida != null) {
                if (lineaLeida.toUpperCase().contains(palabra.toUpperCase())) {
                    // Hay que guardarlo en otro String, .replaceAll() no altera el String que ejecuta el método
                    // Reemplazo la palabra con espacios antes y después, para que no la cuente si la cadena de caracteres
                    // aparece como parte de otra palabra más grande, pero así no cogerá las que están al principio y final de línea
                    String lineaAlterada = lineaLeida.toUpperCase().replaceAll(palabra.toUpperCase(), signoSustituto);
                    numOcurrencias += ocurrenciaDeUnCaracter(lineaAlterada, signoSustituto);
                }
                lineaLeida = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numOcurrencias;
    }

    public static int ocurrenciaDeUnaPalabra(String linea,String palabra){
        int numOcurrencias=0;
        String signoSustituto="+";
        if (linea.toUpperCase().contains(palabra.toUpperCase())) {
                    // Hay que guardarlo en otro String, .replaceAll() no altera el String que ejecuta el método
                    // Reemplazo la palabra con espacios antes y después, para que no la cuente si la cadena de caracteres
                    // aparece como parte de otra palabra más grande, pero así no cogerá las que están al principio y final de línea
                    String lineaAlterada = linea.toUpperCase().replaceAll(palabra.toUpperCase(), signoSustituto);
                    numOcurrencias += ocurrenciaDeUnCaracter(lineaAlterada, signoSustituto);
                }
        return numOcurrencias;
    }
    
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

    public static int contarLetras(File fichero) {
        int numLetras = 0;
        try {
            FileReader fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);

            String lineaLeida = br.readLine();
            while (lineaLeida != null) {
                numLetras += contarLetras(lineaLeida);
                lineaLeida = br.readLine();
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numLetras;
    }  
    
    public static File invertirLetrasDeLineas(File fichero) {
        File ficheroDelReves = new File(OperacionesFicheros.getCanonicalPathSinExtension(fichero) + "DelReves." + OperacionesFicheros.getExtension(fichero));

        FileReader fr;
        try {
            fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(ficheroDelReves);
            BufferedWriter bw = new BufferedWriter(fw);

            String lineaLeida = br.readLine();
            while (lineaLeida != null) {
                String lineaDelReves = invertirLetras(lineaLeida);
                // Escribimos la línea
                bw.write(lineaDelReves);
                // Pasamos a nueva línea
                bw.newLine();
                // Guardamos los cambios en el fichero ('enviamos' lo que hay guardado en el buffer)
                bw.flush();
                lineaLeida=br.readLine();
            }

            br.close();
            bw.close();
            fr.close();
            fw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OperacionesReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ficheroDelReves;
    }
    
    public static String invertirLetras(String string){
        String stringInvertido="";       
        for (int i = string.length()-1; i >=0; i--) {
            stringInvertido=stringInvertido+string.charAt(i);
        }
        return stringInvertido;
    }

    


}
