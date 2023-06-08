package at.fhtw.swen2.tutorial.service;

import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

public interface PDFGeneratorService {
    void fileExists(File file);
    Document generateReport(String report) throws IOException;
    void writeTourReport(Document document) throws IOException;
    void writeSummaryReport(Document document) throws IOException;
}
