/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreLine;
import gt.edu.usac.sun.item_analysis_tool.model.ScoreResumen;
import gt.edu.usac.sun.item_analysis_tool.model.TestProccedData;
import gt.edu.usac.sun.item_analysis_tool.report.ReportType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.stereotype.Service;

/**
 *
 * @author emayu
 */
@Service
public class ReportService {
     @Autowired
    private ObjectMapper objectMapper;
     
    
    public byte[] getExportedReportInBytes(String jasperFileName, String type, TestProccedData data) {
        return getExportedReportInBytes(jasperFileName, type, null, data);

    }
    
    public byte[] getExportedReportInBytes(String jasperFileName, String type, Map<String, Object> reportParams, TestProccedData data) {
        ReportType reportType = getReportType(type);
        try {
            //fill report
            JasperPrint jprint = generateJasperPrintFrom(jasperFileName, reportParams, data);
            //export report
            return export(reportType, jprint);
        } catch (JRException | JsonProcessingException ex) {
            Logger.getLogger(ReportService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Ocurrio un problema al generar el reporte ", ex);
        }

    }
    
    public ReportType getReportType(String type){
        try {
            return  ReportType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(String.format("El tipo de reporte '%s' no es soportado", type));
        }
    }
    
    public TestProccedData filterResultReportData(TestProccedData data){
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
    
    private JasperPrint generateJasperPrintFrom(String reportName, Map<String,Object> params, TestProccedData data) throws JRException, JsonProcessingException{
        String resouceName = "/" + reportName + ".jrxml";
        InputStream resourceStream = getClass().getResourceAsStream(resouceName);
        if(resourceStream == null){
            throw new RuntimeException(String.format("El recurso interno '%s' no fue encontrado", resouceName));
        }
        JasperReport jresport = JasperCompileManager.compileReport(resourceStream);
        byte[] rawInfo = objectMapper.writeValueAsBytes(data);
        InputStream io = new ByteArrayInputStream(rawInfo);
        JsonDataSource dataSource = new JsonDataSource(io);
        return JasperFillManager.fillReport(jresport, params, dataSource);
    }
    
    private byte[] export(ReportType reportType, JasperPrint jprint) throws JRException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        switch (reportType) {
            case PDF:
                JasperExportManager.exportReportToPdfStream(jprint, baos);
                break;
            case XLSX:
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jprint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
                exporter.exportReport();
                break;
        }
        return baos.toByteArray();
    }
}
