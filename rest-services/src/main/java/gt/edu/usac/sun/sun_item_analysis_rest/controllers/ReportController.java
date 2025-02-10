/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreLine;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreResumen;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private JasperPrint generateJasperPrintFrom(String reportName, TestProccedData data) throws JRException, JsonProcessingException{
        return generateJasperPrintFrom(reportName, null, data);
    }
    
    private JasperPrint generateJasperPrintFrom(String reportName, Map<String,Object> params, TestProccedData data) throws JRException, JsonProcessingException{
        InputStream reportTest = getClass().getResourceAsStream(reportName);
        if(reportTest == null){
            throw new RuntimeException(String.format("El recurso interno '%s' no fue encontrado", reportName));
        }
        JasperReport jresport = JasperCompileManager.compileReport(reportTest);
        byte[] rawInfo = objectMapper.writeValueAsBytes(data);
        InputStream io = new ByteArrayInputStream(rawInfo);
        JsonDataSource dataSource = new JsonDataSource(io);
        return JasperFillManager.fillReport(jresport, params, dataSource);
    }    
    
    
    @PostMapping("/scoreDistribution/pdf")
    public ResponseEntity testReportPDF(@RequestBody TestProccedData data) throws JRException, JsonProcessingException{
        final String targetReportName = "/distribucion_de_puntuaciones.jrxml";
        //filling report with data
        JasperPrint jprint = generateJasperPrintFrom(targetReportName, data);
        
        //exporting
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jprint, baos);
        ByteArrayResource bar = new ByteArrayResource(baos.toByteArray());
        
        //builing reponse
        String fileName = LocalDateTime.now() + "_test_report.pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);
    }
    
    @PostMapping("/scoreDistribution/xls")
    public ResponseEntity testReportXLS(@RequestBody TestProccedData data ) throws JRException, JsonProcessingException{
        
        String reportName = ("/distribucion_de_puntuaciones.jrxml");
        JasperPrint jprint = generateJasperPrintFrom(reportName, data);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jprint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();
        
        ByteArrayResource bar = new ByteArrayResource(baos.toByteArray());
        
        String fileName = LocalDateTime.now() + "_test_report.xlsx";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);
    }
    
    private TestProccedData transformData(TestProccedData data){
        final int upperApprovedRecords = 2;
        final int lowerNotApprovedRecords = 4;
        
        ScoreResumen scores = data.getScoreResumen();
        //warranty descend order
        Arrays.sort(scores.getScoreResumen(), (s1, s2) -> s2.getCorrectNumber() - s1.getCorrectNumber());
        int targetIndex=0;
        for(int i=0; i < scores.getScoreResumen().length; i++){
            var line = scores.getScoreResumen()[i];
            if(!line.isApproved()){
                targetIndex = i - 1;
                break;
            }
        }
        //slice array to show only configured recods
        final int startIndex = (targetIndex + 1) - upperApprovedRecords;
        final int finalIndex = (targetIndex + 1) + lowerNotApprovedRecords;
        ScoreLine[] subRecords = Arrays.copyOfRange(scores.getScoreResumen(), startIndex, finalIndex);
        scores.setScoreResumen(subRecords);
        return data;
    }


    @PostMapping("/resultReport/pdf")
    public ResponseEntity resultReportPDF(@RequestParam(name = "subtitle", required = false, defaultValue = "") String subTitleReport, @RequestBody TestProccedData data) throws JRException, JsonProcessingException{
        //filter data for report
        data = transformData(data);
        
        //filling report with data        
        final String targetReportName = "/informe_resultados.jrxml";
        
        Map parms = new HashMap<String, Object>();
        parms.put("subtitle", subTitleReport);
        JasperPrint jprint = generateJasperPrintFrom(targetReportName, parms, data);
        
        //exporting
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jprint, baos);
        ByteArrayResource bar = new ByteArrayResource(baos.toByteArray());
        
        //builing reponse
        String fileName = LocalDateTime.now() + "_informe_de_resultados.pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);   
    }
    
    @PostMapping("/resultReport/xls")
    public ResponseEntity resultReportXLS(@RequestParam(name = "subtitle", required = false, defaultValue = "") String subTitleReport, @RequestBody TestProccedData data ) throws JRException, JsonProcessingException{
        //filter data for report
        data = transformData(data);
        
        //filling report with data        
        final String targetReportName = "/informe_resultados.jrxml";
        
        Map parms = new HashMap<String, Object>();
        parms.put("subtitle", subTitleReport);
        JasperPrint jprint = generateJasperPrintFrom(targetReportName, parms, data);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jprint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();
        
        ByteArrayResource bar = new ByteArrayResource(baos.toByteArray());
        
        String fileName = LocalDateTime.now() + "informe_de_resultados.xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(bar.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(bar);
    }
}
