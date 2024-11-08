/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.exceptions;

/**
 *
 * @author emayu
 */
public class NotFileProvided extends RuntimeException{

    public NotFileProvided() {
        super("El archivo esta vacio o no fue proporcionado");
    }
    
}
