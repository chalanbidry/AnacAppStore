/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author SI-MJLDH
 */
import java.text.DecimalFormat;
 
/**
 * @author fobec.com 2010
 */
public class FileSizeFormater {
 
    /**
     * Constante taille
     */
    private static final long[] CST_SIZE = {1024, 1024 * 1024, 1024 * 1024 * 1024};
    /**
     * Constante unité
     */
    private static final String[] CST_UNITS = {"Ko", "Mo", "Go"};
 
    /**
     * Afficher la taille du fichier formatée
     * @param value taille en octet
     * @return string
     */
    public static String format(final long value) {
        String result = null;
        long size;
        for (int i = 0; i < 3; i++) {
            size=value/CST_SIZE[i];
            if (size <= 1024) {
                result = mergeUnit(size, CST_UNITS[i]);
                break;
            }
        }
        return result;
    }
 
    /**
     * Arrondir et ajouter l'unité
     * @param size
     * @param unit
     * @return
     */
    public static String mergeUnit(long size, String unit) {
        return new DecimalFormat("#,##0.#").format(size) + " " + unit;
    }
 
    /**
     * Exemple
     * @param args
     */
    public static void main(final String[] args) {
            System.out.println(format(123456));
    }
}
