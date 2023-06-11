package at.fhtw.swen2.tutorial.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Transactional
@Service
public class ImportLogsServiceImpl implements ImportDataService<Log> {
    private final LogRepository logRepository;
    private final LogMapper logMapper;

    private static Logger logger = LogManager.getLogger(ImportLogsServiceImpl.class);

    public ImportLogsServiceImpl(LogRepository logRepository, LogMapper logMapper) {
        logger.debug("ImportLogsServiceImpl created");
        this.logRepository = logRepository;
        this.logMapper = logMapper;
    }


    @Override
    public List<Log> importData(File file) throws IOException {

        List<Log> data = new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Skip header row
            if (row.getRowNum() == 0) {
                continue;
            }
            if (row.getCell(1) == null) {
                break;
            }

            Long id = (long) row.getCell(1).getNumericCellValue();
            System.out.println("ID: " + id);
            Long tourId = (long) row.getCell(2).getNumericCellValue();
            System.out.println("Tour ID: " + tourId);
//            String dateTime = row.getCell(2).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
            String dateTime = row.getCell(3).getStringCellValue();
            String comment = row.getCell(4).getStringCellValue();
            String difficulty = row.getCell(5).getStringCellValue();
            String totalTime = String.valueOf(row.getCell(6).getNumericCellValue());
//            String totalTime = "12:30";
            int rating = (int) row.getCell(7).getNumericCellValue();

            LogEntity log = LogEntity.builder()
                    .id(id)
                    .tourId(tourId)
                    .dateTime(dateTime)
                    .comment(comment)
                    .difficulty(difficulty)
                    .totalTime(totalTime)
                    .rating(rating)
                    .build();
            logger.info("Record inserted for ID: " + id);
            data.add(logMapper.fromEntity(log));
        }

        workbook.close();
        fileInputStream.close();

        logger.info("Data imported successfully.");

        return data;
    }
}
