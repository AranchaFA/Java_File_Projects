package Modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignosCastellanos {

    public static List<Integer> caracteresCastellanosASCII() {
        List<Integer> listaLetras = new ArrayList<>();

        for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
            Integer integer = i;
            listaLetras.add(integer); // Hay que pasarlo a Integer ¿?
        }
        for (int i = (int) 'a'; i <= (int) 'z'; i++) {
            Integer integer = i;
            listaLetras.add(integer); // Hay que pasarlo a Integer ¿?
        }
        // Añadimos la ñ y las vocales con tilde o diéresis
        Integer[] letrasCastellanas = {(int) 'á', (int) 'é', (int) 'í', (int) 'ó', (int) 'ú', (int) 'Á', (int) 'É', (int) 'Í', (int) 'Ó', (int) 'Ú', (int) 'ñ', (int) 'Ñ', (int) 'ü', (int) 'Ü'};
        listaLetras.addAll(Arrays.asList(letrasCastellanas));

        return listaLetras;
    }

    public static List<String> caracteresCastellanos() {
        List<String> listaLetras = new ArrayList<>();
        List<Integer> listaLetrasASCII = caracteresCastellanosASCII();
        
        for (Integer integer : listaLetrasASCII) {
            listaLetras.add(String.valueOf((char)integer.intValue()));
        }
        
        return listaLetras;
    }

    public static List<Integer> signosPuntuacionSinEspacioASCII() {
        List<Integer> listaSignosPuntuacion = new ArrayList<>();
        List<Integer> caracteresCastellanos = caracteresCastellanosASCII();
        // Incluiremos todos los signos ASCII que no estén entre los caracteres castellanos
        // menos el espacio->32
        for (int i = 0; i <= 255; i++) {
            if (!caracteresCastellanos.contains(i) && i != 32) {
                listaSignosPuntuacion.add(i);
            }
        }
        return listaSignosPuntuacion;
    }
    
    public static List<String> signosPuntuacionSinEspacio() {
        List<String> listaSignosPuntuacion = new ArrayList<>();
        List<Integer> listaGisnosPuntuacionASCII = signosPuntuacionSinEspacioASCII();
        
        for (Integer integer : listaGisnosPuntuacionASCII) {
            listaSignosPuntuacion.add(String.valueOf((char)integer.intValue()));
        }
        
        return listaSignosPuntuacion;
    }

    public static int espacioEnBlancoASCII() {
        return 32;
    }
}
