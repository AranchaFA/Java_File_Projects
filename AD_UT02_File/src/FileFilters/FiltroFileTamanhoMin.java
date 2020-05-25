/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileFilters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author aranx
 */
public class FiltroFileTamanhoMin implements FileFilter {

    private long tamanhoMin;

    public FiltroFileTamanhoMin(long tamanhoMin) {
        this.tamanhoMin = tamanhoMin;
    }

    @Override
    public boolean accept(File file) {
        if (file.length() >= tamanhoMin) {
            return true;
        }
        return false;
    }
}
