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

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ImportLogsServiceImpl implements ImportDataService {
//    public static void main(String[] args) {
//        String excelFilePath = "E:/FH/SS23/SWEN/imports/logs.xlsx";
//        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
//        String dbUsername = "swe2user";
//        String dbPassword = "swe2pw";
//
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            File file = new File(excelFilePath);
//            if (!file.exists()) {
//                System.out.println("Excel file not found.");
//                return;
//            }
//            FileInputStream fileInputStream = new FileInputStream(excelFilePath);
//            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            String insertQuery = "INSERT INTO logs (id, tour_id, date_time, comment, difficulty, total_time, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
//
//            for (Row row : sheet) {
//                // Skip header row
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
//
//                int id = (int) row.getCell(0).getNumericCellValue();
//                int tourId = (int) row.getCell(1).getNumericCellValue();
//                String dateTime = row.getCell(2).getStringCellValue();
//                String comment = row.getCell(3).getStringCellValue();
//                String difficulty = row.getCell(4).getStringCellValue();
//                int totalTime = (int) row.getCell(5).getNumericCellValue();
//                int rating = (int) row.getCell(6).getNumericCellValue();
//
//                // Check if tour_id exists in the tours table
//                boolean tourExists = checkTourExists(connection, tourId);
//
//                if (!tourExists) {
//                    System.out.println("Skipping record with ID: " + id + ". The tour_id does not exist.");
//                    continue;
//                }
//
//                // Check if record already exists in the database
//                boolean recordExists = checkRecordExists(connection, id);
//
//                if (recordExists) {
//                    // Update existing record
//                    updateExistingRecord(connection, id, tourId, dateTime, comment, difficulty, totalTime, rating);
//                    System.out.println("Record updated for ID: " + id);
//                } else {
//                    // Insert new record
//                    insertStatement.setInt(1, id);
//                    insertStatement.setInt(2, tourId);
//                    insertStatement.setString(3, dateTime);
//                    insertStatement.setString(4, comment);
//                    insertStatement.setString(5, difficulty);
//                    insertStatement.setInt(6, totalTime);
//                    insertStatement.setInt(7, rating);
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
//        String selectQuery = "SELECT id FROM logs WHERE id = ?";
//        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//        selectStatement.setInt(1, id);
//        ResultSet resultSet = selectStatement.executeQuery();
//        boolean recordExists = resultSet.next();
//        resultSet.close();
//        selectStatement.close();
//        return recordExists;
//    }
//
//    private static boolean checkTourExists(Connection connection, int tourId) throws SQLException {
//        String selectQuery = "SELECT id FROM tours WHERE id = ?";
//        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
//        selectStatement.setInt(1, tourId);
//        ResultSet resultSet = selectStatement.executeQuery();
//        boolean tourExists = resultSet.next();
//        resultSet.close();
//        selectStatement.close();
//        return tourExists;
//    }
//
//    private static void updateExistingRecord(Connection connection, int id, int tourId, String dateTime, String comment, String difficulty, int totalTime, int rating) throws SQLException {
//        String updateQuery = "UPDATE logs SET tour_id = ?, date_time = ?, comment = ?, difficulty = ?, total_time = ?, rating = ? WHERE id = ?";
//        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//        updateStatement.setInt(1, tourId);
//        updateStatement.setString(2, dateTime);
//        updateStatement.setString(3, comment);
//        updateStatement.setString(4, difficulty);
//        updateStatement.setInt(5, totalTime);
//        updateStatement.setInt(6, rating);
//        updateStatement.setInt(7, id);
//
//        updateStatement.executeUpdate();
//        updateStatement.close();
//    }
    @Autowired
    private LogRepository logRepository;

    @Override
    public void importData(String excelFilePath) throws IOException {
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
