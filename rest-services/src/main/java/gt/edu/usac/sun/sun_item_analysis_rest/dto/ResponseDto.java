/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.dto;

import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;

/**
 *
 * @author Usuario
 */
public class ResponseDto {
    String status;
    String message;
    TestProccedData data;
    String textReport;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TestProccedData getData() {
        return data;
    }

    public void setData(TestProccedData data) {
        this.data = data;
    }

    public String getTextReport() {
        return textReport;
    }

    public void setTextReport(String textReport) {
        this.textReport = textReport;
    }
    
    
    
}
