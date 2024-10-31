/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.controllers;

import gt.edu.usac.sun.item_analysis_tool.analysis.Analizer;
import gt.edu.usac.sun.sun_item_analysis_rest.dto.ResponseDto;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Usuario
 */
@RestController
@RequestMapping("api/files/")
public class FileAnalyzerController {
    
    @PostMapping("/analyze")
        public ResponseEntity<ResponseDto> analyzeFile(@RequestParam("file") MultipartFile file, @RequestParam(name = "includeTextReport", required = false) boolean  includeTextReport){
            var response = new ResponseDto();
            if( file == null || file.isEmpty()){    
                response.setStatus("Error");
                response.setMessage("The file was not provided");
                return ResponseEntity.badRequest().body(response);
            }
            try {
                var isr = new InputStreamReader(file.getInputStream());
                Analizer analizer = new Analizer();
                var  result= analizer.analyze(isr, file.getOriginalFilename());
                
                response.setStatus("OK");
                if(includeTextReport){
                    System.out.println("Including text report");   
                    response.setTextReport(analizer.getTextItemStatics(result));
                }
                response.setMessage("Successful");
                response.setData(result);
                return ResponseEntity.ok(response);
            }catch(IOException ioex){
                response.setStatus("Error");
                response.setMessage("Error occured while proccessing the file");
                return ResponseEntity.internalServerError().body(response);
            }
        }
}
