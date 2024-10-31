/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author emayu
 */
public class TestProccedData {
    /**
     * Configuration
     */
    private int itemsNumberConfig;
    private int itemsForAnalize;
    private KeyConfig[] itemKeyConfig;
    private String source;
    
    /** raw data **/
    private long examineesCount;
    private long[] frecuency;
    private Set<String> answerdFounds;
    private Map<String, Long>[] itemKeyCount;
    private Duration duration;
    
    /** analyzed data **/
    private ScoreResumen scoreResumen;
    private ItemStatics responseStatics;

    public int getItemsNumberConfig() {
        return itemsNumberConfig;
    }

    public void setItemsNumberConfig(int itemsNumberConfig) {
        this.itemsNumberConfig = itemsNumberConfig;
    }

    public KeyConfig[] getItemKeyConfig() {
        return itemKeyConfig;
    }

    public void setItemKeyConfig(KeyConfig[] item_keyConfig) {
        this.itemKeyConfig = item_keyConfig;
    }

    public long getExamineesCount() {
        return examineesCount;
    }

    public void setExamineesCount(long answerCount) {
        this.examineesCount = answerCount;
    }

    public long[] getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(long[] frecuency) {
        this.frecuency = frecuency;
    }

    public int getItemsForAnalize() {
        return itemsForAnalize;
    }

    public void setItemsForAnalize(int itemsAnalized) {
        this.itemsForAnalize = itemsAnalized;
    }

    public Map<String, Long>[] getItemKeyCount() {
        return itemKeyCount;
    }

    public void setItemKeyCount(Map<String, Long>[] itemKeyCount) {
        this.itemKeyCount = itemKeyCount;
    }

    public Set<String> getAnswerdFounds() {
        return answerdFounds;
    }

    public void setAnswerdFounds(Set<String> answerdFounds) {
        this.answerdFounds = answerdFounds;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ScoreResumen getScoreResumen() {
        return scoreResumen;
    }

    public void setScoreResumen(ScoreResumen scoreResumen) {
        this.scoreResumen = scoreResumen;
    }

    public ItemStatics getItemResponseStatics() {
        return responseStatics;
    }

    public void setItemResponseStatics(ItemStatics responseStatics) {
        this.responseStatics = responseStatics;
    }
    
    
    
    
    @Override
    public String toString() {
        return "TestData{" + "itemsNumberConfig=" + itemsNumberConfig + ", itemsAnalized=" + itemsForAnalize + ", item_keyConfig=" + Arrays.toString(itemKeyConfig) + '}';
    }
    

    
    
    
    
    
}
