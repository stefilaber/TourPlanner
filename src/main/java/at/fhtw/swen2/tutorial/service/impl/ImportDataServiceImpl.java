package at.fhtw.swen2.tutorial.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.fhtw.swen2.tutorial.service.ImportDataService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportDataServiceImpl implements ImportDataService {
    public static void main(String[] args) {
        String excelFilePath = "E:/FH/SS23/SWEN/imports/tours.xlsx";
        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
        String dbUsername = "swe2user";
        String dbPassword = "swe2pw";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            String insertQuery = "INSERT INTO tours (id, name, tour_description, tour_from, tour_to, transport_type, tour_distance, estimated_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                String tourDescription = row.getCell(2).getStringCellValue();
                String tourFrom = row.getCell(3).getStringCellValue();
                String tourTo = row.getCell(4).getStringCellValue();
                String transportType = row.getCell(5).getStringCellValue();
                double tourDistance = row.getCell(6).getNumericCellValue();
                int estimatedTime = (int) row.getCell(7).getNumericCellValue();

                // Check if record already exists in the database
                boolean recordExists = checkRecordExists(connection, id);

                if (recordExists) {
                    // Update existing record
                    updateExistingRecord(connection, id, name, tourDescription, tourFrom, tourTo, transportType, tourDistance, estimatedTime);
                    System.out.println("Record updated for ID: " + id);
                } else {
                    // Skip duplicate record
                    insertStatement.setInt(1, id);
                    insertStatement.setString(2, name);
                    insertStatement.setString(3, tourDescription);
                    insertStatement.setString(4, tourFrom);
                    insertStatement.setString(5, tourTo);
                    insertStatement.setString(6, transportType);
                    insertStatement.setDouble(7, tourDistance);
                    insertStatement.setInt(8, estimatedTime);
                    insertStatement.executeUpdate();
                    System.out.println("Record inserted for ID: " + id);
                }
            }

            insertStatement.close();
            workbook.close();
            fileInputStream.close();

            System.out.println("Data imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkRecordExists(Connection connection, int id) throws SQLException {
        String selectQuery = "SELECT id FROM tours WHERE id = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        selectStatement.setInt(1, id);
        ResultSet resultSet = selectStatement.executeQuery();
        boolean recordExists = resultSet.next();
        resultSet.close();
        selectStatement.close();
        return recordExists;
    }

    private static void updateExistingRecord(Connection connection, int id, String name, String tourDescription, String tourFrom, String tourTo, String transportType, double tourDistance, int estimatedTime) throws SQLException {
        String updateQuery = "UPDATE tours SET name = ?, tour_description = ?, tour_from= ?, tour_to = ?, tour_distance = ?, transport_type = ?, estimated_time = ? WHERE id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setString(1, name);
        updateStatement.setString(2, tourDescription);
        updateStatement.setString(3, tourFrom);
        updateStatement.setString(4, tourTo);
        updateStatement.setDouble(5, tourDistance);
        updateStatement.setString(6, transportType);
        updateStatement.setInt(7, estimatedTime);
        updateStatement.setInt(8, id);
        updateStatement.executeUpdate();
        updateStatement.close();
    }
}

