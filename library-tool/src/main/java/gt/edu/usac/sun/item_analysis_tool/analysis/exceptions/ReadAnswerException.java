/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.analysis.exceptions;

/**
 *
 * @author emayu
 */
public class ReadAnswerException extends RuntimeException{

    public ReadAnswerException(String message) {
        super(message);
    }

    public ReadAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
