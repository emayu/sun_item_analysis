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
            throw new ConfigException("Configuración invalida, el parametró configLines no fue proporcionado correctamente");
        }
        String[] firstLineConfig = configLines[0].split(" ");
        int amountOfQuestion = 0;
        if(firstLineConfig.length <= 1){
            throw new ConfigException("No se encontro una configuración válida(falta el espacio en blanco),  Linea 1");
        }
        try{
            amountOfQuestion = Integer.parseInt(firstLineConfig[1]);
        }catch(NumberFormatException ex){
            throw new ConfigException("No se encontro una configuración válida(cantidad de items), Linea 1");
        }
        
        config.setItemsNumberConfig(amountOfQuestion);
        
        TreeMap<String, Long> itemKeyCount[] = new TreeMap[config.getItemsNumberConfig()];
        
        //read examen key line
        if(configLines[1] == null){
            throw new ConfigException("La configuración de la clave no fue proporcionada, Linea 2");
        }else if(configLines[1].length()!= config.getItemsNumberConfig()){
            throw new ConfigException("La configuración de la clave no tiene el tamaño indicado, Linea 2");
        }else if(configLines[2] == null){
            throw new ConfigException("La configuración cantidad posibles respuestas no fue proporcionada, Linea 3");
        }else if(configLines[2].length() != config.getItemsNumberConfig()){
            throw new ConfigException("La configuración cantidad posibles respuestas no tiene el tamaño indicado, Linea 3");
        }else if(configLines[3] == null){
            throw new ConfigException("La configuración activación de items no fue proporcionada, Linea 4");
        }else if(configLines[3].length() != config.getItemsNumberConfig()){
            throw new ConfigException("La configuración activación de items no tiene el tamaño indicado, Linea 4");
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
                throw new ConfigException("La configuración cantidad respuestas no es un valor correcto, Linea 3, " + (item +1));
            }
            
            //read actived line
            char inputConfig = configLines[3].charAt(item);
            if(!(inputConfig == 'Y' 
                    || inputConfig == 'N'
                    || inputConfig == 'y'
                    || inputConfig == 'n')){
                throw new ConfigException(String.format("El valor '%s' no es válido, Linea 4, %d", inputConfig, item + 1));
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



