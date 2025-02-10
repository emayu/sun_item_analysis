/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.exceptions;

import gt.edu.usac.sun.item_analysis_tool.analysis.exceptions.ConfigException;
import gt.edu.usac.sun.item_analysis_tool.analysis.exceptions.ReadAnswerException;
import gt.edu.usac.sun.sun_item_analysis_rest.dto.ErrorResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *
 * @author emayu
 */
@RestControllerAdvice
public class CustomExeptionHandler {
    
    @ExceptionHandler({ConfigException.class, ReadAnswerException.class, NotFileProvided.class})
    public ResponseEntity  handle(Exception ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return new ResponseEntity(new ErrorResponse(status, ex.getMessage(), stringWriter.toString()), status);
    }
    
    @ExceptionHandler({FileTypeNotAccepted.class})
    public ResponseEntity  handle(FileTypeNotAccepted ex){
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return new ResponseEntity(new ErrorResponse(status, ex.getMessage(), stringWriter.toString()), status);
    }
        
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
     public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest r) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity(new ErrorResponse(httpStatus, "Se ha excedido el tamaño máximo de carga", ""), httpStatus);
    }
    
    
    
}
