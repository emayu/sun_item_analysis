/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

/**
 *
 * @author emayu
 */
public class SingleSyllabusSeparator {
    
    
    
    private int amoutOfAnswers;
    
    public String tranform(String info, String source, String syllabus, String[] config){
        StringReader reader = new StringReader(info);
        return tranform(reader, source, syllabus);
    }
    public String tranform(File file, String syllabus, String[] config) throws FileNotFoundException{
         FileReader reader = new FileReader(file);
         return tranform(reader, file.getAbsolutePath(), syllabus);
    }
    
    
    private String tranform(Reader reader, String sourceTitle, String syllabus){
        StringBuilder builder = new StringBuilder();
//        try(BufferedReader dr = new BufferedReader(reader)){
//            
//        }
        return builder.toString();
    }
}
