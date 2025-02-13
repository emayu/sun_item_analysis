/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.item_analysis_tool.report;

/**
 *
 * @author emayu
 */
public enum ReportType {
    
    PDF(".pdf", "application/pdf"),
    XLSX(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    
    private final String extension;
    private final String mediaType;

    private ReportType(String extension, String mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMediaType() {        
        return mediaType;
    }
    
    
    
    
}
