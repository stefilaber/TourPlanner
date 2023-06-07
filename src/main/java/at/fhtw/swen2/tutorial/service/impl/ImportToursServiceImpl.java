package at.fhtw.swen2.tutorial.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ImportToursServiceImpl implements ImportDataService {
//    public static void main(String[] args) {
//        String excelFilePath = "E:/FH/SS23/SWEN/imports/tours.xlsx";
//        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
//        String dbUsername = "swe2user";
//        String dbPassword = "swe2pw";
//
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath));
//            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            String insertQuery = "INSERT INTO tours (id, name, tour_description, tour_from, tour_to, transport_type, tour_distance, estimated_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
//
//            for (Row row : sheet) {
//                // Skip header row
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
//
//                int id = (int) row.getCell(0).getNumericCellValue();
//                String name = row.getCell(1).getStringCellValue();
//                String tourDescription = row.getCell(2).getStringCellValue();
//                String tourFrom = row.getCell(3).getStringCellValue();
//                String tourTo = row.getCell(4).getStringCellValue();
//                String transportType = row.getCell(5).getStringCellValue();
//                double tourDistance = row.getCell(6).getNumericCellValue();
//                int estimatedTime = (int) row.getCell(7).getNumericCellValue();
//
//                // Check if record already exists in the database
//                boolean recordExists = checkRecordExists(connection, id);
//
//                if (recordExists) {
//                    // Update existing record
//                    updateExistingRecord(connection, id, name, tourDescription, tourFrom, tourTo, transportType, tourDistance, estimatedTime);
//                    System.out.println("Record updated for ID: " + id);
//                } else {
//                    // Skip duplicate record
//                    insertStatement.setInt(1, id);
//                    insertStatement.setString(2, name);
//                    insertStatement.setString(3, tourDescription);
//                    insertStatement.setString(4, tourFrom);
//                    insertStatement.setString(5, tourTo);
//                    insertStatement.setString(6, transportType);
//                    insertStatement.setDouble(7, tourDistance);
//                    insertStatement.setInt(8, estimatedTime);
//                    insertStatement.executeUpdate();
//                    System.out.println("Record inserted for ID: " + id);
//                }
//            }
//
//            insertStatement.close();
//            workbook.close();
//            fileInputStream.close();
//
//            System.out.println("Data imported successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static boolean checkRecordExists(Connection connection, int id) throws SQLException {
//        String selectQuery = "SELECT id FROM tours WHERE id = ?";
//        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//        selectStatement.setInt(1, id);
//        ResultSet resultSet = selectStatement.executeQuery();
//        boolean recordExists = resultSet.next();
//        resultSet.close();
//        selectStatement.close();
//        return recordExists;
//    }
//
//    private static void updateExistingRecord(Connection connection, int id, String name, String tourDescription, String tourFrom, String tourTo, String transportType, double tourDistance, int estimatedTime) throws SQLException {
//        String updateQuery = "UPDATE tours SET name = ?, tour_description = ?, tour_from= ?, tour_to = ?, tour_distance = ?, transport_type = ?, estimated_time = ? WHERE id = ?";
//        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//        updateStatement.setString(1, name);
//        updateStatement.setString(2, tourDescription);
//        updateStatement.setString(3, tourFrom);
//        updateStatement.setString(4, tourTo);
//        updateStatement.setDouble(5, tourDistance);
//        updateStatement.setString(6, transportType);
//        updateStatement.setInt(7, estimatedTime);
//        updateStatement.setInt(8, id);
//        updateStatement.executeUpdate();
//        updateStatement.close();
//    }

    @Autowired
    private LogRepository logRepository;

    public void importLogs(String excelFilePath) throws IOException {
        File file = new File(excelFilePath);
        if (!file.exists()) {
            System.out.println("Excel file not found.");
            return;
        }

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
            int totalTime = (int) row.getCell(5).getNumericCellValue();
            int rating = (int) row.getCell(6).getNumericCellValue();

            LogEntity log = logRepository.findById(id).orElse(null);
            if (log != null) {
                // Update existing record
                log.setTourId(tourId);
                log.setDateTime(dateTime);
                log.setComment(comment);
                log.setDifficulty(difficulty);
                log.setTotalTime(totalTime);
                log.setRating(rating);
                logRepository.save(log);
                System.out.println("Record updated for ID: " + id);
            } else {
                // Insert new record
                log = LogEntity.builder()
                        .id(id)
                        .tourId(tourId)
                        .dateTime(dateTime)
                        .comment(comment)
                        .difficulty(difficulty)
                        .totalTime(totalTime)
                        .rating(rating)
                        .build();
                logRepository.save(log);
                System.out.println("Record inserted for ID: " + id);
            }
        }

        workbook.close();
        fileInputStream.close();

        System.out.println("Data imported successfully.");
    }
}

