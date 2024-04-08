package com.transtu.reclamationservice.service;

import com.transtu.reclamationservice.entities.Reclamation;
import com.transtu.reclamationservice.repository.ReclamationRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportReclamationService {
    @Autowired
    private ReclamationRepository repository;


    public String exportReport(String reportFormat, String query,LocalDate fromDate,LocalDate toDate, String typeAccidentIncident,String typeDegat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\lenovo\\Desktop\\pfe";
        List<Reclamation> reclamations = repository.searchReclamationsByDateRangeAndTypes(query,fromDate,toDate,typeAccidentIncident,typeDegat);
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reclamation.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reclamations);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Responsable TRANSTU");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\reclamations.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\reclamations.pdf");
        }
        if (reportFormat.equalsIgnoreCase("csv")){
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\reclamations.csv");
        }

        return "report generated in path : " + path;
    }

    public String exportReportWithoutDate(String reportFormat, String query, String typeAccidentIncident,String typeDegat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\lenovo\\Desktop\\pfe";
        List<Reclamation> reclamations = repository.searchReclamationsWithoutDateRange(query,typeAccidentIncident,typeDegat);
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reclamation.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reclamations);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Responsable TRANSTU");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\reclamations.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\reclamations.pdf");
        }
        if (reportFormat.equalsIgnoreCase("csv")){
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\reclamations.csv");
        }

        return "report generated in path : " + path;
    }


}
