package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.Tour;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

public interface PDFGeneratorService {
    void fileExists(File file);
    Document generateReport(String report) throws IOException;
    void writeTourReport(String report, Tour tour) throws IOException;
    void writeSummaryReport(String report) throws IOException;
}
