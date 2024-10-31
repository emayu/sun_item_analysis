/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.model;

/**
 *
 * @author emayu
 */
public class KeyConfig {
    private String value;
    private boolean activated;
    private int amount;

    public KeyConfig() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int count) {
        this.amount = count;
    }    

    @Override
    public String toString() {
        return "KeyConfig{" + "value=" + value + ", activated=" + activated + ", amount=" + amount + '}';
    }
    
    
}
