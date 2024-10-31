/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.analysis;

import gt.edu.usac.sun.item_analysis_tool.model.ItemStatic;
import gt.edu.usac.sun.item_analysis_tool.model.ItemStatics;
import gt.edu.usac.sun.item_analysis_tool.model.ResponseFrequency;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;

import java.util.List;
import java.util.Map;

/**
 *
 * @author emayu
 */
public class ItemStaticExtractor {
    public static ItemStatics getStatics(TestProccedData data){
        ItemStatics statics = new ItemStatics();
        List<ItemStatic> list = statics.getStatics();
        
        for(int item = 0; item < data.getItemsNumberConfig(); item++){
            Map<String, Long> responseStatics = data.getItemKeyCount()[item];
            if (!data.getItemKeyConfig()[item].isActivated()) {
                continue;
            }
            ItemStatic lineStatic = new ItemStatic();
            lineStatic.setItem(String.format("item %d", (item + 1)));
            for(Map.Entry<String, Long> entry : responseStatics.entrySet()){
                ResponseFrequency frequency = new ResponseFrequency();
                String actualKey = data.getItemKeyConfig()[item].getValue();
                frequency.setValue(entry.getKey());
                frequency.setFrecuency(entry.getValue());
                frequency.setProportion((double) entry.getValue()/data.getExamineesCount());
                if(entry.getKey().equals(actualKey)){
                   lineStatic.setDificulty(frequency.getProportion());
                   frequency.setIsKey(true);
                }
                lineStatic.getResponsesFrequency().add(frequency);
            }
            list.add(lineStatic);
        }
        return statics;
    }
}
