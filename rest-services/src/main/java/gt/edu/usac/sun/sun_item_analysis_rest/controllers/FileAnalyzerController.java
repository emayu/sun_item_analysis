/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.controllers;

import gt.edu.usac.sun.sun_item_analysis_rest.exceptions.NotFileProvided;
import gt.edu.usac.sun.sun_item_analysis_rest.exceptions.FileTypeNotAccepted;
import gt.edu.usac.sun.item_analysis_tool.analysis.Analizer;
import gt.edu.usac.sun.sun_item_analysis_rest.dto.ResponseDto;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author emayu
 */
@RestController
@RequestMapping("api/files/")
public class FileAnalyzerController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileAnalyzerController.class);
    
    @PostMapping("/analyze")
    public ResponseEntity<ResponseDto> analyzeFile(@RequestParam("file") MultipartFile file, @RequestParam(name = "includeTextReport", required = false) boolean includeTextReport) throws IOException {
        var response = new ResponseDto();
        if (file == null || file.isEmpty()) {
            throw new NotFileProvided();
        }
        
        if(!"text/plain".equals(file.getContentType())){
            int dotIndex = file.getOriginalFilename().lastIndexOf(".");
            String extension = dotIndex > 0 ? file.getOriginalFilename().substring(dotIndex) : file.getContentType();
            throw new FileTypeNotAccepted(String.format("El tipo de achivo '%s' no es soportado", extension));
        }

        try {
            var isr = new InputStreamReader(file.getInputStream());
            Analizer analizer = new Analizer();
            var result = analizer.analyze(isr, file.getOriginalFilename());
            response.setStatus("OK");
            if (includeTextReport) {
                response.setTextReport(analizer.getTextItemStatics(result));
            }
            response.setMessage("Successful");
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (IOException ioex) {
            logger.error("Error al intentar analizar el archivo", ioex);
            throw ioex;
        }
    }
    
        
    
}
