/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.analysis;

import gt.edu.usac.sun.item_analysis_tool.model.ItemStatic;
import gt.edu.usac.sun.item_analysis_tool.model.ItemStatics;
import gt.edu.usac.sun.item_analysis_tool.model.ResponseFrequency;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreLine;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreResumen;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;

/**
 *
 * @author emayu
 */
public class Analizer {
    
    public TestProccedData analyze(String info, String source){
        StringReader reader = new StringReader(info);
        return analyze(reader, source);
    }
    public TestProccedData analyze(File file) throws FileNotFoundException{
         FileReader reader = new FileReader(file);
         return analyze(reader, file.getAbsolutePath());
         
    }
    
    public TestProccedData analyze(Reader reader, String sourceTitle){
        Instant start = Instant.now();
        System.out.printf("Analyzing from: %s\n", sourceTitle);
        try(BufferedReader dr = new BufferedReader(reader)){
            //read config
            TestDataCreator configCreator = new TestDataCreator(dr);
            TestProccedData data = configCreator.createConfig();
            data.setSource(sourceTitle);
            System.out.println(data);
            
            //read answers
            String lineText;
            long examineesCount = 0;
            for(long lineNumber = configCreator.getLastReadLine() + 1; (lineText = dr.readLine())!= null; lineNumber++ ){
                //System.out.println(lineText);
                if(lineText.length() != data.getItemsNumberConfig()){
                    throw new RuntimeException(String.format("Error se encontraron %d respuestas, se esperaban %d, Linea %d", lineText.length(), data.getItemsNumberConfig(), lineNumber));
                }
                int countValid = 0;
                for(int anwser=0; anwser < data.getItemsNumberConfig(); anwser++){
                    String answerInput = lineText.substring(anwser, anwser + 1);
                    
                    var keyConfig = data.getItemKeyConfig()[anwser];
                    
                    if(keyConfig.isActivated()){
                        if(keyConfig.getValue().equals(answerInput)){
                            countValid++;
                        }
                        //modify key hit
                        Map<String, Long> itemKeyCount = data.getItemKeyCount()[anwser];
                        Long actualCount = itemKeyCount.get(answerInput);
                        if(actualCount == null){
                            itemKeyCount.put(answerInput, 1L);
                        }else{
                            itemKeyCount.put(answerInput, ++actualCount);
                        }
                        //check all found answers
                        if(!data.getAnswerdFounds().contains(answerInput)){
                            data.getAnswerdFounds().add(answerInput);
                        }
                    }
                }
                long actualFrecuency = data.getFrecuency()[countValid];
                data.getFrecuency()[countValid] = ++actualFrecuency;
                examineesCount++;
            }
            data.setExamineesCount(examineesCount);
            //process result 
            ScoreResumen resumen = generateResumen(data);
            ItemStatics responseStatics = ItemStaticExtractor.getStatics(data);
            
            //finished procced time
            Instant finish = Instant.now();
            Duration duration = Duration.between(start, finish);
            data.setDuration(duration);
            data.setItemResponseStatics(responseStatics);
            data.setScoreResumen(resumen);
            
            return data;
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public void printStatic(TestProccedData data, Appendable o) throws IOException{
        printItemStatics(data, o);
        printScoreDistributionResumen(data.getScoreResumen(), data.getDuration(), data.getSource());
    }
    
    public String getTextItemStatics(TestProccedData data) throws IOException{
        StringBuilder builder = new StringBuilder();
        printItemStatics(data, builder);
        return builder.toString();
    }
    
    
    
    private void printItemStatics(TestProccedData data, Appendable o) throws IOException{
        Formatter fm = new Formatter(o);
        o.append(System.lineSeparator());
        o.append("Anális de Frecuencias").append(System.lineSeparator());
        
        fm.format("Fuente de análisis: %s\n", data.getSource());
        fm.format("Fecha: %s\n", new Date());
        var duration = data.getDuration();
        fm.format("Duración %d secs %d milliseconds\n", duration.toSecondsPart(), duration.toMillisPart());
        final String bar = "=====================================================================================";
        for( ItemStatic item : data.getItemResponseStatics().getStatics()){
            fm.format("%s - Dificultad: %1.4f\n", item.getItem(), item.getDificulty());
            o.append(bar).append(System.lineSeparator());
            fm.format("%4s %6s %-6s %6s %6s %6s %3s\n", "Res.",  "Valor", "Frecu-", "Prop.", "------", "------", "---");
            fm.format("%4s %6s %-6s %6s %6s %6s %3s\n",     "",       "",  "encia", "Total", "------", "------", "---");
            fm.format("%4s %6s %-6s %6s %6s %6s %3s\n", "----", "------", "------", "------", "------", "------", "---");
            for(ResponseFrequency frec: item.getResponsesFrequency()){
                fm.format("%4s %-6s %6d %6.2f %6s %6s %3s\n", ( frec.isIsKey() ? "*": ""), frec.getValue(), frec.getFrecuency(), frec.getProportion(), "------", "------", "---");
            }
            o.append(System.lineSeparator());
            o.append(System.lineSeparator());
        }
    }
    
    private void printScoreDistributionResumen(ScoreResumen resumen, Duration duration, String sourceTitle) {
        String bar = "*************************";
        String finalBar0 = "----+----+----+----+----+";
        String finalBar1 = "    5   10   15   20   25";

        System.out.println("");
        System.out.println("Distribución de puntuaciones");
        System.out.printf("Fuente de análisis: %s\n", sourceTitle);
        System.out.printf("Fecha: %s\n", new Date());
        System.out.printf("Duración %d secs %d milliseconds\n", duration.toSecondsPart(), duration.toMillisPart());
        System.out.println("");
        System.out.printf("%7s %6s %6s %6s %6s %6s %3s\n", "=======", "======", "======", "======", "======", "======", "===");
        System.out.printf("%7s %6s %6s %6s %6s %6s %3s\n", "Puntaje", "#", "frec.", "frec.", "AprobX", "%Aprob", "ctc");
        System.out.printf("%7s %6s %6s %6s %6s %6s %3s\n", "x corre", "corre", "      ", "acum.", "corre.", "Xcorre", "");
        System.out.printf("%7s %6s %6s %6s %6s %6s %3s\n", "-------", "------", "------", "------", "------", "------", "---");

        for (var line : resumen.getScoreResumen()) {
            //Data table part
            System.out.printf("%6.2f%1s %6d %6d %6d %6d %6.2f %3.0f %5.2f", line.getScore(), (line.isApproved() ? "*" : " "), line.getCorrectNumber(), line.getFrequency(), line.getCumulativeFrecuency(), line.getApprovedCount(), line.getApprovedPercentage(), line.getPct(), line.getPct());

            //graph part
            System.out.printf("%s%s\n", (line.getCorrectNumber() % 5 == 0 ? "+" : "|"), bar.substring(0, line.getPctInt()));
        }
        System.out.printf("%78s\n", finalBar0);
        System.out.printf("%78s\n", finalBar1);
    }
    
    private ScoreResumen generateResumen(TestProccedData data){
        ScoreResumen resumen = new ScoreResumen();
        ScoreLine[] lines = new ScoreLine[data.getItemsForAnalize() + 1];
        final long[] frecuencies = data.getFrecuency();
        final double scorePerItem = (double)100 / data.getItemsForAnalize();
        for(int i=0; i < lines.length; i++){
            ScoreLine line = new ScoreLine();
            line.setCorrectNumber(i);
            line.setFrequency(frecuencies[i]);
            if(i == 0){
                line.setCumulativeFrecuency(frecuencies[i]);
            }else{
                line.setCumulativeFrecuency(lines[i - 1].getCumulativeFrecuency() + line.getFrequency());
            }
            line.setPct((double)line.getFrequency()*100 /data.getExamineesCount());
            line.setScore(line.getCorrectNumber() * scorePerItem);
            line.setApproved(Math.round(line.getScore()) >= 61);//redondear nota
            
            lines[i] = line;
        }
        for(int i=lines.length -1; i >=0; i--){
            ScoreLine inverse = lines[i];
            if(i == lines.length - 1){
                inverse.setApprovedCount(inverse.getFrequency());
            }else{
                inverse.setApprovedCount(lines[ i + 1].getApprovedCount() + inverse.getFrequency());
            }
            inverse.setApprovedPercentage((double)(inverse.getApprovedCount() *100) / data.getExamineesCount());
        }
        resumen.setScoreResumen(lines);
        return resumen;
                
    }
    
}
