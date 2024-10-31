/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author emayu
 */
public class ItemStatic {
    private String item;
    private double dificulty;
    private List<ResponseFrequency> responsesFrequency;

    public ItemStatic() {
        responsesFrequency = new LinkedList<>();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getDificulty() {
        return dificulty;
    }

    public void setDificulty(double dificulty) {
        this.dificulty = dificulty;
    }

    public List<ResponseFrequency> getResponsesFrequency() {
        return responsesFrequency;
    }

    public void setResponsesFrequency(List<ResponseFrequency> responsesFrequency) {
        this.responsesFrequency = responsesFrequency;
    }
    
}
