package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.PDFGeneratorService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Statistic;
import at.fhtw.swen2.tutorial.service.dto.Tour;
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
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// annotate to create service into bin that can be injected into controller to use it
@Service
@Transactional
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    final TourService tourService;
    final LogService logService;
    private static Logger logger = LogManager.getLogger(PDFGeneratorServiceImpl.class);

    public PDFGeneratorServiceImpl(TourService tourService, LogService logService) {
        logger.debug("PDFGeneratorServiceImpl created");
        this.tourService = tourService;
        this.logService = logService;
    }

    @Override
    public void fileExists(File file) {
        // delete existing PDF file
        if (file.exists()) {
            if (file.delete()) {
                logger.info("Existing PDF file deleted successfully.");
            } else {
                logger.info("Failed to delete the existing PDF file.");
            }
        } else {
            logger.info("No existing PDF file found.");
        }
    }

    @Override
    public Document generateReport(String report) throws IOException {

        logger.debug("generateReport called");

        String path = new java.io.File(".").getCanonicalPath() + "\\target\\reports\\";
        Files.createDirectories(Paths.get(path));

        PdfWriter writer = new PdfWriter(report);
        PdfDocument pdf = new PdfDocument(writer);

        logger.debug("PDF file created successfully.");
        return new Document(pdf);
    }

    @Override
    public void writeTourReport(String report, Tour tour) throws IOException {

        logger.debug("writeTourReport called");
        Document document = generateReport(report);
        java.util.List<Log> logList = logService.getLogList(tour.getId());
        System.out.println("logList:" + logList);
        final String MAP_IMAGE = "src/main/resources/maps/" + tour.getName() + ".png";

        final String name = tour.getName();
        final String info = "Tour information";
        final String map = "Route information";

        Paragraph tourReportHeader = new Paragraph(name)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(18)
                .setBold()
                .setUnderline()
                .setFontColor(ColorConstants.PINK);
        document.add(tourReportHeader);
        document.add(new Paragraph(tour.getTourDescription()));

        Paragraph listHeader = new Paragraph("\n" + info)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        List list = new List()
                .setListSymbol("• ")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));
        list.add(new ListItem("From: " + tour.getTourFrom()))
                .add(new ListItem("To: " + tour.getTourTo()))
                .add(new ListItem("Transport: " + tour.getTransportType()))
                .add(new ListItem("Distance: " + tour.getTourDistance() + " km"))
                .add(new ListItem("Estimated duration: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.ofSecondOfDay(tour.getEstimatedTime()))));

        document.add(listHeader);
        document.add(list);

        Paragraph tableHeader = new Paragraph("\n" + "Tour Logs")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tableHeader);

        Table table = new Table(UnitValue.createPercentArray(new float[] {20, 50, 13, 12, 5})).useAllAvailableWidth();
        table.addHeaderCell(getHeaderCell("Date/Time"));
        table.addHeaderCell(getHeaderCell("Comment"));
        table.addHeaderCell(getHeaderCell("Difficulty"));
        table.addHeaderCell(getHeaderCell("Total time"));
        table.addHeaderCell(getHeaderCell("Rating"));
        table.setFontSize(12).setBackgroundColor(ColorConstants.WHITE);

        for (Log log : logList) {
            table.addCell(log.getDateTime());
            table.addCell(log.getComment());
            table.addCell(log.getDifficulty());
            table.addCell(String.valueOf(log.getTotalTime()));
            table.addCell(String.valueOf(log.getRating()));
        }

        document.add(table);

        document.add(new AreaBreak());

        Paragraph imageHeader = new Paragraph(map)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(imageHeader);
        ImageData imageData = ImageDataFactory.create(MAP_IMAGE);
        document.add(new Image(imageData));

        logger.info("Tour report created successfully.");
        document.close();
    }

    @Override
    public void writeSummaryReport(String report) throws IOException {

        logger.debug("writeSummaryReport called");
        Document document = generateReport(report);

        java.util.List<Statistic> tourStatsList;
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        final String header = "Tours Summary Report";

        // calculate average time, difficulty and rating for each tour
        tourStatsList = calculateStatistics();

        Paragraph tourReportHeader = new Paragraph(header)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(18)
                .setBold()
                .setUnderline()
                .setFontColor(ColorConstants.PINK);
        document.add(tourReportHeader);

        Paragraph tableHeader = new Paragraph("Statistics")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setBold()
                .setFontColor(ColorConstants.PINK);
        document.add(tableHeader);

        Table table = new Table(UnitValue.createPercentArray(new float[] {40, 18, 22, 20})).useAllAvailableWidth();
        table.addHeaderCell(getHeaderCell("Tour"));
        table.addHeaderCell(getHeaderCell("Average time"));
        table.addHeaderCell(getHeaderCell("Average difficulty"));
        table.addHeaderCell(getHeaderCell("Average rating"));
        table.setFontSize(12).setBackgroundColor(ColorConstants.WHITE);

        for (Statistic statistic : tourStatsList) {
            if(statistic.getAvgTime() == "0" && statistic.getAvgRating() == 0 && statistic.getAvgDifficulty().equals("0")) {
                table.addCell(statistic.getTourName() + " (No logs for this tour yet!)");
                table.addCell("-");
                table.addCell("-");
                table.addCell("-");
            } else {
                table.addCell(statistic.getTourName());
                table.addCell(statistic.getAvgTime());
                table.addCell(statistic.getAvgDifficulty());
                table.addCell(df.format(statistic.getAvgRating()));
            }
        }

        document.add(table);

        logger.info("Summary report created successfully.");
        document.close();
    }
    private Cell getHeaderCell(String s) {
        return new Cell().add(new Paragraph(s)).setBold().setBackgroundColor(ColorConstants.GRAY);
    }

    private java.util.List<Statistic> calculateStatistics() {
        java.util.List<Tour> tourList = tourService.getTourList();
        java.util.List<Statistic> tourStatsList = new ArrayList<>();

        for (Tour tour : tourList) {
            long avgTime = 0;
            int easy = 0;
            int medium = 0;
            int hard = 0;
            String avgDifficulty;
            double avgRating = 0;

            java.util.List<Log> logList = logService.getLogList(tour.getId());

            if (logList.size() > 0) {
                for (Log log : logList) {
                    avgTime += convertToSeconds(log.getTotalTime());
                    if (log.getDifficulty().equals("easy")) {
                        easy++;
                    } else if (log.getDifficulty().equals("medium")) {
                        medium++;
                    } else {
                        hard++;
                    }
                    avgRating += log.getRating();
                }

                avgTime /= logList.size();
                avgRating /= logList.size();

                if (easy > medium && easy > hard) {
                    avgDifficulty = "easy";
                } else if (medium > easy && medium > hard) {
                    avgDifficulty = "medium";
                } else if (hard > easy && hard > medium){
                    avgDifficulty = "hard";
                } else if (easy > medium) {
                    avgDifficulty = "medium";
                } else if (easy == medium && easy > hard) {
                    avgDifficulty = "easy/medium";
                } else if (medium > easy) {
                    avgDifficulty = "medium/hard";
                } else {
                    avgDifficulty = "medium";
                }
                tourStatsList.add(new Statistic(tour.getName(), DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.ofSecondOfDay(avgTime)), avgDifficulty, avgRating));

            } else {
                tourStatsList.add(new Statistic(tour.getName(), "0", String.valueOf(0), 0));
            }

        }
        logger.debug("calculateStatistics finished");
        return tourStatsList;
    }

    private int convertToSeconds(String time){
        logger.debug("convertToSeconds called");
        String[] timeArray = time.split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = 0;
        if(timeArray.length >= 2) {
            minutes = Integer.parseInt(timeArray[1]);
        }
        int seconds = 0;
        //check if there are seconds in the time string
        if(timeArray.length == 3){
            seconds = Integer.parseInt(timeArray[2]);
        }

        return hours*3600 + minutes*60 + seconds;
    }
}
