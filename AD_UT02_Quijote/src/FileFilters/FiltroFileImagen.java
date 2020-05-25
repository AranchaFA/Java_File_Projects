/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileFilters;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author aranx
 */
public class FiltroFileImagen implements FileFilter {

    String[] arrayExtensiones = {"JPG", "GIF", "PNG"};
    List<String> listaExtensiones = new ArrayList<>(Arrays.asList(arrayExtensiones));

    public FiltroFileImagen() {
    }

    public FiltroFileImagen(String[] arrayExtensiones) {
        this.arrayExtensiones = arrayExtensiones;
    }

    public FiltroFileImagen(List<String> listaExtensiones) {
        this.listaExtensiones = listaExtensiones;
    }

    @Override
    public boolean accept(File file) {
        StringTokenizer stringTokenizer = new StringTokenizer(file.getName(), ".");
        stringTokenizer.nextToken();
        String extension = "";
        if (stringTokenizer.hasMoreTokens()) {
            extension = stringTokenizer.nextToken();
        }
        if (listaExtensiones.contains(extension.toUpperCase())) {
            return true;
        }
        return false;
    }
}
