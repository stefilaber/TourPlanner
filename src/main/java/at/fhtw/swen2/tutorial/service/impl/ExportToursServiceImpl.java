package at.fhtw.swen2.tutorial.service.impl;
import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import at.fhtw.swen2.tutorial.service.ExportDataService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.mapper.TourMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ExportToursServiceImpl implements ExportDataService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    public ExportToursServiceImpl(TourRepository tourRepository, TourMapper tourMapper) {
        log.debug("ExportToursServiceImpl created");
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
    }

    public void exportData() throws IOException {

        List<TourEntity> tourEntities = tourRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Tours");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("From");
        headerRow.createCell(4).setCellValue("To");
        headerRow.createCell(5).setCellValue("Transport Type");
        headerRow.createCell(6).setCellValue("Distance");
        headerRow.createCell(7).setCellValue("Estimated Time");

        int rowNumber = 1;
        for (TourEntity tourEntity : tourEntities) {
            Tour tour = tourMapper.fromEntity(tourEntity);
            Row dataRow = sheet.createRow(rowNumber);
            dataRow.createCell(0).setCellValue(tour.getId());
            dataRow.createCell(1).setCellValue(tour.getName());
            dataRow.createCell(2).setCellValue(tour.getTourDescription());
            dataRow.createCell(3).setCellValue(tour.getTourFrom());
            dataRow.createCell(4).setCellValue(tour.getTourTo());
            dataRow.createCell(5).setCellValue(tour.getTransportType());
            dataRow.createCell(6).setCellValue(tour.getTourDistance());
            dataRow.createCell(7).setCellValue(tour.getEstimatedTime());
            rowNumber++;
        }

        String path = new java.io.File(".").getCanonicalPath() + "\\src\\main\\resources\\export_data\\";
        Files.createDirectories(Paths.get(path));
        FileOutputStream fileOutputStream = new FileOutputStream(path+"tours.xlsx");
        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();

        log.info("Data exported successfully.");

    }
}
