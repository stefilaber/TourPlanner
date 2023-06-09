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

@Transactional
@Service
public class ImportLogsServiceImpl implements ImportDataService<Log> {
    private final LogRepository logRepository;
    private final LogMapper logMapper;

    public ImportLogsServiceImpl(LogRepository logRepository, LogMapper logMapper) {
        this.logRepository = logRepository;
        this.logMapper = logMapper;
    }


    @Override
    public List<Log> importData(File file) throws IOException {

        List<Log> data= new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Skip header row
            if (row.getRowNum() == 0) {
                continue;
            }

            Long id = (long) row.getCell(0).getNumericCellValue();
            Long tourId = (long) row.getCell(1).getNumericCellValue();
            String dateTime = row.getCell(2).getStringCellValue();
            String comment = row.getCell(3).getStringCellValue();
            String difficulty = row.getCell(4).getStringCellValue();
            String totalTime = row.getCell(5).getStringCellValue();
            int rating = (int) row.getCell(6).getNumericCellValue();

            LogEntity log = LogEntity.builder()
                    .id(id)
                    .tourId(tourId)
                    .dateTime(dateTime)
                    .comment(comment)
                    .difficulty(difficulty)
                    .totalTime(totalTime)
                    .rating(rating)
                    .build();
            System.out.println("Record inserted for ID: " + id);
            data.add(logMapper.fromEntity(log));
        }

        workbook.close();
        fileInputStream.close();

        System.out.println("Data imported successfully.");

        return data;
    }
}
