/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileFilters;

import Modelo.OperacionesFicheros;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtra la extensión del archivo (con o sin punto ".", p.ej. "txt" o ".txt")
 *
 * @author aranx
 */
public class FiltroFileExtension implements FileFilter {

    private List<String> listaExtensiones;

    public FiltroFileExtension(String extension) {
        listaExtensiones=new ArrayList<String>();
        // Si introducimos la extension con "." delante lo eliminamos, para que no provoque errores al comparar cadenas en el accept
        if (extension.charAt(0) == '.') {
            extension = extension.substring(1);
        }
        listaExtensiones.add(extension.toUpperCase());
    }

    public FiltroFileExtension(List<String> listaExtensiones) {
        this.listaExtensiones=new ArrayList<String>();
        // Si introducimos la extension con "." delante lo eliminamos, para que no provoque errores al comparar cadenas en el accept
        for (String extension : listaExtensiones) {
            if (extension.charAt(0) == '.') {
                extension = extension.substring(1).toUpperCase();
            }
        }
        this.listaExtensiones.addAll(listaExtensiones);
    }

    @Override
    public boolean accept(File file) {
        String extensionFile=OperacionesFicheros.getExtension(file);
        // Con lista.contains(extension) peta, no pilla el toUpperCase y da NullPointerException ¿?
        for (String extension : listaExtensiones) {
            if (extension.equalsIgnoreCase(extensionFile)) {
                return true;
            }
        }
        return false;
    }

}
