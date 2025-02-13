/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import gt.edu.usac.sun.item_analysis_tool.report.ReportType;
import gt.edu.usac.sun.sun_item_analysis_rest.services.ReportService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author emayu
 */
@RestController
@RequestMapping("api/report/")
public class ReportController {
    
    ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @PostMapping("/scoreDistribution/{type}")
    public ResponseEntity scoreDistributionReport(@PathVariable String type, @RequestBody TestProccedData data) throws JRException{
        final String targetReportName = "distribucion_de_puntuaciones";
        //generate report in bytes
        byte[] reportDataInBytes = reportService.getExportedReportInBytes(targetReportName, type, data);
        ByteArrayResource bar = new ByteArrayResource(reportDataInBytes);
        
        //builing reponse
        ReportType reportType = reportService.getReportType(type);
        String fileName = LocalDateTime.now() + "_" + targetReportName + reportType.getExtension();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(reportType.getMediaType()));
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);
    }

    @PostMapping("/resultReport/{type}")
    public ResponseEntity resultReport(
            @PathVariable String type,
            @RequestParam(name = "subtitle", required = false, defaultValue = "") String subTitleReport,
            @RequestBody TestProccedData data) throws JRException, JsonProcessingException{
        
        //filter data for report
        data = reportService.filterResultReportData(data);
        
        final String targetReportName = "informe_de_resultados";
        //filling report params
        Map parms = new HashMap<String, Object>();
        parms.put("subtitle", subTitleReport);
        
        
        //generate report in bytes
        byte[] reportInBytes = reportService.getExportedReportInBytes(targetReportName, type, parms, data);
        ByteArrayResource bar = new ByteArrayResource(reportInBytes);
        
        //builing reponse
        ReportType reportType = reportService.getReportType(type);
        String fileName = LocalDateTime.now() + "_" + targetReportName + reportType.getExtension();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(reportType.getMediaType()));
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);   
    }
}
