
package Modelo;

import java.io.File;
import java.util.Comparator;

public class ComparatorsFiles {
    
    // Comparar por tamaño del fichero
    public static class ComparatorFileTamanho implements Comparator<File> {

        /**
         * Compara dos ficheros por su tamaño (de mayor a menor)
         * @param file1
         * @param file2
         * @return +1 si fichero1>fichero2, -1 si fichero1<fichero2, 0 si fichero1=fichero2
         */
        @Override
        public int compare(File file1, File file2) {
            if (file1.length()>file2.length()) {
                return 1;
            } else if (file1.length()<file2.length()) {
                return -1;
            }
            return 0;
        }

        /**
         * Compara dos ficheros por su tamaño (de menor a mayor)
         * @return  -1 si fichero1>fichero2, +1 si fichero1<fichero2, 0 si fichero1=fichero2
         */
        @Override
        public Comparator<File> reversed() {
            return Comparator.super.reversed();
        }
        
    }
    
}
