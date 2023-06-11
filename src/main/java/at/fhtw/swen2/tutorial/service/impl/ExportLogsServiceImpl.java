package at.fhtw.swen2.tutorial.service.impl;
import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.ExportDataService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
@Transactional
public class ExportLogsServiceImpl implements ExportDataService {

    private final LogRepository logRepository;
    private final LogMapper logMapper;

    private static Logger logger = LogManager.getLogger(ExportLogsServiceImpl.class);

    public ExportLogsServiceImpl(LogRepository logRepository, LogMapper logMapper) {
        logger.debug("ExportLogsServiceImpl created");
        this.logRepository = logRepository;
        this.logMapper = logMapper;
    }

    public void exportData() throws IOException {
        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
        String dbUsername = "swe2user";
        String dbPassword = "swe2pw";
        String excelFilePath = "E:/FH/SS23/SWEN/exports/logs.xlsx";

//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            Statement statement = connection.createStatement();
//            String selectQuery = "SELECT * FROM logs";
//            ResultSet resultSet = statement.executeQuery(selectQuery);
        List<LogEntity> logEntities = logRepository.findAll();

            XSSFWorkbook workbook = new XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Logs");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tour ID");
            headerRow.createCell(2).setCellValue("Date/Time");
            headerRow.createCell(3).setCellValue("Comment");
            headerRow.createCell(4).setCellValue("Difficulty");
            headerRow.createCell(5).setCellValue("Total Time");
            headerRow.createCell(6).setCellValue("Rating");

            int rowNumber = 1;
            for (LogEntity logEntity : logEntities) {
                Log log = logMapper.fromEntity(logEntity);
                Row dataRow = sheet.createRow(rowNumber);
                dataRow.createCell(0).setCellValue(log.getId());
                dataRow.createCell(1).setCellValue(log.getTourId());
                dataRow.createCell(2).setCellValue(log.getDateTime());
                dataRow.createCell(3).setCellValue(log.getComment());
                dataRow.createCell(4).setCellValue(log.getDifficulty());
                dataRow.createCell(5).setCellValue(log.getTotalTime());
                dataRow.createCell(6).setCellValue(log.getRating());
                rowNumber++;
            }

//            resultSet.close();
//            statement.close();

            //create folder if it doesn't exist
        String path = new java.io.File(".").getCanonicalPath() + "\\src\\main\\resources\\export_data\\";
        Files.createDirectories(Paths.get(path));
        FileOutputStream fileOutputStream = new FileOutputStream(path+"logs.xlsx");

        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();

        logger.debug("Exported logs to excel file");
    }
}
