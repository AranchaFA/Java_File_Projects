/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author aranx
 */
public class Vista {

    /**
     * Muestra una lista de ficheros por pantalla
     *
     * @param listaAMostrar
     */
    public static void mostrarListaDeFicheros(List<File> listaAMostrar) throws IOException {
        for (File fileLeido : listaAMostrar) {
            // Para mostrar la fecha de última modificación
            GregorianCalendar fechaModificacion = new GregorianCalendar();
            fechaModificacion.setTimeInMillis(fileLeido.lastModified());
            String fechaString = fechaModificacion.DATE + "/" + fechaModificacion.MONTH + "/" + fechaModificacion.YEAR;
            String horaString = fechaModificacion.HOUR_OF_DAY + ":" + fechaModificacion.MINUTE + ":" + fechaModificacion.SECOND;
            System.out.println(fileLeido.getCanonicalPath() + "   Tamaño: " + fileLeido.length() + "  Modificado: " + fechaString + "  " + horaString);
        }
    }

    /**
     * Muestra una colección Map de listas de ficheros por pantalla
     *
     * @param mapAMostrar
     */
    public static void mostrarMapDeFicheros(Map<String, List> mapAMostrar) throws IOException {
        Set<String> keySet = mapAMostrar.keySet();
        for (String key : keySet) {
            List listaSubdirectorios = mapAMostrar.get(key);
            mostrarListaDeFicheros(listaSubdirectorios);
        }
    }

    public static void mostrarMapRecuentoStrings(Map<String, Integer> mapAMostrar) {
        Set<String> keySet = mapAMostrar.keySet();
        for (String key : keySet) {
            System.out.println(key + ":" + mapAMostrar.get(key));;
        }
    }

    public static void mostrarMapRecuentoStringsSubficheros(Map<File, Map<String, Integer>> map) throws IOException {
        for (File file : map.keySet()) {
            System.out.println("----- " + file.getCanonicalPath() + "----------");
            mostrarMapRecuentoStrings(map.get(file));
        }
    }
}
