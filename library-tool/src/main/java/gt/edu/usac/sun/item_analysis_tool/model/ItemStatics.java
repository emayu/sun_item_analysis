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
public class ItemStatics {
    private List<ItemStatic> statics ;

    public ItemStatics() {
        statics = new LinkedList<>();
    }
    

    public List<ItemStatic> getStatics() {
        return statics;
    }

    public void setStatics(List<ItemStatic> statics) {
        this.statics = statics;
    }
    
}
