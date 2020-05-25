
package Controlador;

import Modelo.OperacionesBufferedFiles;
import Modelo.OperacionesFicheros;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;

public class Main_Quijote {

    public static void main(String[] args) throws IOException {
        
        /*        System.out.println(OperacionesBufferedFiles.ocurrenciasPalabra("   hola   , me llamo arancha holaholaholaholaholaholahola hola", "hola"));
        
        System.out.println(OperacionesBufferedFiles.contarLineas(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote.txt")));
        
        System.out.println(OperacionesBufferedFiles.ocurrenciasPalabra(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote2.txt"), "QUijOtE"));
        
        System.out.println(OperacionesBufferedFiles.contarLetras("hola soy yo     000"));
        
        OperacionesBufferedFiles.revertirLetrasDeLineas(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote2.txt"));
        */
        /*Map<String,Integer> mapa=OperacionesBufferedFiles.densidadPalabras(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote2.txt"));
        Vista.Vista.mostrarMapRecuentoStrings(mapa);
        System.out.println(mapa.size());
        OperacionesFicheros.limpiarSignosPuntuacion(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote2.txt"));
        */
        /*Map<Integer, File> mapCapitulos = OperacionesBufferedFiles.desglosarCapitulos(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\quijote.txt"));
        for (Integer capitulo : mapCapitulos.keySet()) {
        System.out.println("Capítulo "+capitulo+mapCapitulos.get(capitulo).getCanonicalPath());
        }*/
        
        Vista.Vista.mostrarMapRecuentoStringsSubficheros(OperacionesBufferedFiles.densidadPalabrasSubficheros(new File("C:\\Users\\aranx\\OneDrive\\Documentos\\DAM 2\\AD\\UT02\\ficherosNecesarios\\subficheros")));
    }
    
    
}
