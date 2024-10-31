/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.model;

/**
 *
 * @author emayu
 */
public class ScoreLine {
    int correctNumber;
    long frequency;
    long cumulativeFrecuency;
    long approvedCount;
    double approvedPercentage;
    double pct;
    double score;
    boolean approved;

    public ScoreLine() {
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumer) {
        this.correctNumber = correctNumer;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public long getCumulativeFrecuency() {
        return cumulativeFrecuency;
    }

    public void setCumulativeFrecuency(long cumulativeFrecuency) {
        this.cumulativeFrecuency = cumulativeFrecuency;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public double getApprovedPercentage() {
        return approvedPercentage;
    }

    public void setApprovedPercentage(double approvedPercentage) {
        this.approvedPercentage = approvedPercentage;
    }

    public double getPct() {
        return pct;
    }
    
    public int getPctInt(){
        return (int)Math.round(pct);
    }

    public void setPct(double pct) {
        this.pct = pct;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
    
    
}
