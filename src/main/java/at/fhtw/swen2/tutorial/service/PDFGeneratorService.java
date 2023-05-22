package at.fhtw.swen2.tutorial.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

// annotate to create service into bin that can be injected into controller to use it
@Service
public class PDFGeneratorService {

    // TODO: replace with real data
    public static final String TOUR_DESCRIPTION = "Embark on a captivating journey from Vienna to Salzburg and immerse yourself in the rich cultural heritage and stunning landscapes of Austria. Begin in Vienna, where you'll explore opulent palaces, visit iconic landmarks like St. Stephen's Cathedral, and stroll along the elegant Ringstrasse Boulevard. Leaving the city behind, enjoy a scenic drive through the Austrian countryside, filled with charming villages and vineyards, as you make your way to Salzburg.\n" +
                                                  "\n" +
                                                  "In Salzburg, be enchanted by its baroque architecture and UNESCO-listed historic center. Take a guided walking tour, visit Mozart's birthplace, and explore filming locations from \"The Sound of Music.\" Don't miss the awe-inspiring Hohensalzburg Fortress and indulge in traditional Austrian cuisine before bidding farewell. This Vienna to Salzburg tour promises an unforgettable experience, combining captivating history, musical heritage, and breathtaking landscapes in one enchanting adventure.";
    public static final String GOOGLE_MAPS_PNG = "src/main/resources/google_maps.png";
    public static final String TOUR_REPORT = "target/tourReport.pdf";
    public static final String SUMMARY_REPORT = "target/summaryReport.pdf";
    public static final File tourReportFile = new File(TOUR_REPORT);
    public static final File summaryReportFile = new File(SUMMARY_REPORT);
    public static void main(String[] args ) throws IOException {
        fileExists(tourReportFile);
        fileExists(summaryReportFile);

        writeTourReport(generateReport(TOUR_REPORT));
        writeSummaryReport(generateReport(SUMMARY_REPORT));
    }

    private static void fileExists(File file) {
        // delete existing PDF file
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Existing PDF file deleted successfully.");
            } else {
                System.out.println("Failed to delete the existing PDF file.");
            }
        } else {
            System.out.println("No existing PDF file found.");
        }
    }

    public static Document generateReport(String report) throws IOException {
        PdfWriter writer = new PdfWriter(report);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        return document;
    }

    public static void writeTourReport(Document document) throws IOException {
        // TODO: replace with real data
        final String header = "Tour name";
        final String info = "Tour information";
        final String map = "Route information";

        Paragraph tourReportHeader = new Paragraph(header)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tourReportHeader);
        document.add(new Paragraph(TOUR_DESCRIPTION));

        Paragraph listHeader = new Paragraph("\n" + info)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        List list = new List()
                .setListSymbol("• ")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        list.add(new ListItem("From: Vienna"))
                .add(new ListItem("To: Salzburg"))
                .add(new ListItem("Transport: Bus"))
                .add(new ListItem("Distance: 300 km"))
                .add(new ListItem("Estimated duration: 3 hours"));
        document.add(listHeader);
        document.add(list);

        Paragraph tableHeader = new Paragraph("\n" + "Tour Logs Table")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tableHeader);
        Table table = new Table(UnitValue.createPercentArray(new float[] {20, 50, 10, 15, 5})).useAllAvailableWidth();
        table.addHeaderCell(getHeaderCell("Date/Time"));
        table.addHeaderCell(getHeaderCell("Comment"));
        table.addHeaderCell(getHeaderCell("Difficulty"));
        table.addHeaderCell(getHeaderCell("Total time"));
        table.addHeaderCell(getHeaderCell("Rating"));
        table.setFontSize(14).setBackgroundColor(ColorConstants.WHITE);
        table.addCell("May 1, 2023, 9:00 AM");
        table.addCell("We started our tour in Vienna and explored the magnificent Schönbrunn Palace. The opulent interiors and sprawling gardens were truly a sight to behold.");
        table.addCell("easy");
        table.addCell("3 hours");
        table.addCell("4.5");
        table.addCell("May 2, 2023, 10:30 AM");
        table.addCell(" Today, we visited St. Stephen's Cathedral in Vienna. The Gothic architecture was stunning, and we had the opportunity to climb the tower for panoramic views of the city.");
        table.addCell("medium");
        table.addCell("2 hours");
        table.addCell("4");
        document.add(table);

        document.add(new AreaBreak());

        Paragraph imageHeader = new Paragraph(map)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(imageHeader);
        ImageData imageData = ImageDataFactory.create(GOOGLE_MAPS_PNG);
        document.add(new Image(imageData));

        document.close();
    }

    public static void writeSummaryReport(Document document) throws IOException {
        final String header = "Tours Summary Report";
        // TODO: replace with real data
        final long avgTime = 0;
        final long avgDistance = 0;
        final long avgRating = 0;

        Paragraph tourReportHeader = new Paragraph(header)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tourReportHeader);

        Paragraph tableHeader = new Paragraph("\n" + "Tour Logs Table")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tableHeader);
        Table table = new Table(UnitValue.createPercentArray(new float[] {40, 18, 22, 20})).useAllAvailableWidth();
        table.addHeaderCell(getHeaderCell("Tour name"));
        table.addHeaderCell(getHeaderCell("Average time"));
        table.addHeaderCell(getHeaderCell("Average distance"));
        table.addHeaderCell(getHeaderCell("Average rating"));
        table.setFontSize(14).setBackgroundColor(ColorConstants.WHITE);
        table.addCell("Wienerberg Hiking Trail");
        table.addCell("3 hours");
        table.addCell("5 km");
        table.addCell("4.5");
        table.addCell("Vienna City Tour");
        table.addCell("2 hours");
        table.addCell("10 km");
        table.addCell("4");
        document.add(table);

        document.close();
    }
    private static Cell getHeaderCell(String s) {
        return new Cell().add(new Paragraph(s)).setBold().setBackgroundColor(ColorConstants.GRAY);
    }
}
