/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileFilters;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;

/**
 * Filtra los ficheros que han sido modificados desde un determinado periodo de tiempo
 * que especificamos por parámetros (en segundos)
 * @author aranx
 */
public class FiltroFileUltimaModificacion implements FileFilter {

    private int segundosDesdeModificacion;

    public FiltroFileUltimaModificacion(int segundosDesdeModificacion) {
        this.segundosDesdeModificacion = segundosDesdeModificacion;
    }

    @Override
    public boolean accept(File file) {
        // Fecha actual
        Calendar fechaLimite = Calendar.getInstance();
        // Le restamos los segundos pasados por parámetro a la fecha actual
        fechaLimite.add(Calendar.SECOND, -segundosDesdeModificacion);
        // Fecha de última modificación (tiempo en ms)
        long milisegundosUltimaModificacion = file.lastModified();
        // Comparamos la fecha de modificación del fichero con la fecha límite
        if (fechaLimite.getTimeInMillis() < milisegundosUltimaModificacion) {
            return true;
        }
        return false;
    }
}
