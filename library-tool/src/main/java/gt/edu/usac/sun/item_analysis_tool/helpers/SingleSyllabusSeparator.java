/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.helpers;

import gt.edu.usac.sun.item_analysis_tool.analysis.TestDataCreator;
import gt.edu.usac.sun.item_analysis_tool.analysis.exceptions.ReadAnswerException;
import gt.edu.usac.sun.item_analysis_tool.analysis.exceptions.ReadSourceException;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.util.logging.Logger;

/**
 *
 * @author emayu
 */
public class SingleSyllabusSeparator {
    
    
    public String transform(String info, String source, String syllabus, String[] config){
        StringReader reader = new StringReader(info);
        return transform(reader, source, syllabus, config);
    }
    public String transform(File file, String syllabus, String[] config) throws FileNotFoundException{
         FileReader reader = new FileReader(file);
         return transform(reader, file.getAbsolutePath(), syllabus, config);
    }
    
    
    public String transform(Reader reader, String sourceTitle, String syllabus, String[] config){
        Instant start = Instant.now();
        
        StringBuilder builder = new StringBuilder();
        LOG.info(() -> "Transforming from: " + sourceTitle);
        try(BufferedReader dr = new BufferedReader(reader)){
            //read config
            TestDataCreator configCreator = new TestDataCreator(config);
            TestProccedData data = configCreator.createConfig();
            data.setSource(sourceTitle);
            LOG.info(data.toString());
            
            //input config is ok
            builder.append(config[0]).append(System.lineSeparator());
            builder.append(config[1]).append(System.lineSeparator());
            builder.append(config[2]).append(System.lineSeparator());
            builder.append(config[3]).append(System.lineSeparator());
            
            //read answers
            String inputLine;
            for(long lineNumber = 1; (inputLine = dr.readLine())!= null; lineNumber++){
                String[] temp = inputLine.split(syllabus);
                if(temp.length <= 1){
                    throw new ReadAnswerException(String.format("No se encontró el temario indicado %s en archivo entrada, Linea %d", syllabus, lineNumber));
                }
                String answers = temp[1];
                if(answers.length() != data.getItemsNumberConfig()){
                    throw new ReadAnswerException(String.format("El número de respuestas en el archivo de entrada es diferente al indicado en la configuración %d, Linea %d", data.getItemsNumberConfig(), lineNumber));
                }
                builder.append(answers).append(System.lineSeparator());
            }
            return builder.toString();
        }catch(IOException io){
            throw new ReadSourceException("Ocurrio un problema al leer la fuente", io);
        }
    }
    private static final Logger LOG = Logger.getLogger(SingleSyllabusSeparator.class.getName());
}
