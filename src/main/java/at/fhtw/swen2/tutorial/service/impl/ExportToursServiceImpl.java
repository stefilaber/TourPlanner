package at.fhtw.swen2.tutorial.service.impl;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import at.fhtw.swen2.tutorial.service.ExportDataService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExportToursServiceImpl implements ExportDataService {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
        String dbUsername = "swe2user";
        String dbPassword = "swe2pw";
        String excelFilePath = "E:/FH/SS23/SWEN/exports/tours.xlsx";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT * FROM tours";
            ResultSet resultSet = statement.executeQuery(selectQuery);

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
            while (resultSet.next()) {
                Row dataRow = sheet.createRow(rowNumber);
                dataRow.createCell(0).setCellValue(resultSet.getInt("id"));
                dataRow.createCell(1).setCellValue(resultSet.getString("name"));
                dataRow.createCell(2).setCellValue(resultSet.getString("tour_description"));
                dataRow.createCell(3).setCellValue(resultSet.getString("tour_from"));
                dataRow.createCell(4).setCellValue(resultSet.getString("tour_to"));
                dataRow.createCell(5).setCellValue(resultSet.getString("transport_type"));
                dataRow.createCell(6).setCellValue(resultSet.getDouble("tour_distance"));
                dataRow.createCell(7).setCellValue(resultSet.getInt("estimated_time"));
                rowNumber++;
            }

            resultSet.close();
            statement.close();

            FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();

            System.out.println("Data exported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
