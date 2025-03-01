/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.analysis;

import gt.edu.usac.sun.item_analysis_tool.analysis.exceptions.ConfigException;
import gt.edu.usac.sun.item_analysis_tool.model.KeyConfig;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author emayu
 */
public class TestDataCreator {
    
    private final String configLines[];
    private final BufferedReader br;
    private int lastReadLine;

    public TestDataCreator(BufferedReader reader) {
        this.br = reader;
        this.configLines = new String[4];
    }
    
    public TestDataCreator(String[] config){
        this.br = null;
        this.configLines = config;
    }
    
    public TestProccedData createConfig() throws IOException, ConfigException{
        String lineText;
        for(this.lastReadLine = 1; this.br != null && (lineText = this.br.readLine())!= null; this.lastReadLine++ ){
                System.out.println(lineText);
                //read only first four line
                if(this.lastReadLine == 1){
                    configLines[0] = lineText;
                }else if(this.lastReadLine == 2){
                    configLines[1] = lineText;
                }else if(this.lastReadLine == 3){
                    configLines[2] = lineText;
                }else if(this.lastReadLine == 4){
                    configLines[3] = lineText;
                    break;
                }
        }
        TestProccedData config = new TestProccedData();
        
        if(this.configLines == null || this.configLines.length < 3){
            throw new ConfigException("Configuración inválida, el parámetro configLines no fue proporcionado correctamente");
        }
        
        String firstLineConfig = configLines[0];
        if(firstLineConfig == null || firstLineConfig.isBlank()){
            throw new ConfigException("No se encontró una configuración válida(línea vacía),  Línea 1");
        }
        if(firstLineConfig.length() < 3){
            throw new ConfigException("No se encontró una configuración válida(cantidad de items), Línea 1 columna 1 a 3");
        }
        //first line contains amount of question in the first three columns, 
        //with hundreds in the column 1, tens in column 2 
        //and units in column 3
        //"  1" is valid because units are in the third column
        short trimColumn = 0;
        if(firstLineConfig.charAt(0) == ' '){
            trimColumn++;
        }
        if(firstLineConfig.charAt(1) == ' '){
            trimColumn++;
        }
        int amountOfQuestion = 0;
        try{
            amountOfQuestion = Integer.parseInt(firstLineConfig.substring(trimColumn, 3));
        }catch(NumberFormatException ex){
            throw new ConfigException("No se encontró una configuración válida(cantidad de items), Línea 1.\n"
                    + "Recuerde que la cantidad de items se debe colocar en las columnas 1 a la 3, alineada a la derecha, "
                    + "de ser necesario dejar espacios en blanco.");
        }
        
        config.setItemsNumberConfig(amountOfQuestion);
        
        TreeMap<String, Long> itemKeyCount[] = new TreeMap[config.getItemsNumberConfig()];
        
        //read examen key line
        if(configLines[1] == null){
            throw new ConfigException("La configuración de la clave no fue proporcionada, Línea 2");
        }else if(configLines[1].length()!= config.getItemsNumberConfig()){
            throw new ConfigException("La configuración de la clave no tiene el tamaño indicado, Línea 2");
        }else if(configLines[2] == null){
            throw new ConfigException("La configuración cantidad posibles respuestas no fue proporcionada, Línea 3");
        }else if(configLines[2].length() != config.getItemsNumberConfig()){
            throw new ConfigException("La configuración cantidad posibles respuestas no tiene el tamaño indicado, Línea 3");
        }else if(configLines[3] == null){
            throw new ConfigException("La configuración activación de items no fue proporcionada, Línea 4");
        }else if(configLines[3].length() != config.getItemsNumberConfig()){
            throw new ConfigException("La configuración activación de items no tiene el tamaño indicado, Línea 4");
        }
        
        
        KeyConfig[] items_config = new KeyConfig[config.getItemsNumberConfig()];
        config.setItemsForAnalize(0);
        for(int item=0; item < config.getItemsNumberConfig(); item++){
            var item_keyConfig =  new KeyConfig();
            
            //read examen key value
            item_keyConfig.setValue(configLines[1].substring(item, item + 1));
            
            // read amout of posible answers line
            int validResposes = 0;
            try{
                validResposes = Integer.parseInt(configLines[2].substring(item, item +1));
            }catch(NumberFormatException ex){
                throw new ConfigException("La configuración cantidad respuestas no es un valor correcto, Línea 3, " + (item +1));
            }
            
            //read actived line
            char inputConfig = configLines[3].charAt(item);
            if(!(inputConfig == 'Y' 
                    || inputConfig == 'N'
                    || inputConfig == 'y'
                    || inputConfig == 'n')){
                throw new ConfigException(String.format("El valor '%s' no es válido, Línea 4, %d", inputConfig, item + 1));
            }
            boolean isActivated = inputConfig == 'Y' || inputConfig == 'y';
            item_keyConfig.setActivated(isActivated);
            if(isActivated){
                config.setItemsForAnalize(1+config.getItemsForAnalize());
            }
            
            itemKeyCount[item] = new TreeMap<String, Long>();
            
            item_keyConfig.setAmount(validResposes);
            items_config[item] = item_keyConfig;
        }
        config.setAnswerdFounds(new TreeSet<String>());
        config.setItemKeyCount(itemKeyCount);
        config.setFrecuency(new long[config.getItemsNumberConfig() + 1]);
        config.setItemKeyConfig(items_config);
        return config;
    }

    public int getLastReadLine() {
        return lastReadLine;
    }
    
    
}



