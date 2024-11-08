/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.dto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import org.springframework.http.HttpStatus;

/**
 *
 * @author emayu
 */
public class ErrorResponse {
    
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String trace;

    public ErrorResponse() {
        this.timestamp = new Date();
    }
    
    public ErrorResponse(HttpStatus httpStatus, String message){
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
    }
    
    public ErrorResponse(HttpStatus httpStatus, String message, String trace){
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = message;
        this.trace = trace;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }
    
    
    
    
}
