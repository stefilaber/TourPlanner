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
public class ExportLogsServiceImpl implements ExportDataService {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/swe2db";
        String dbUsername = "swe2user";
        String dbPassword = "swe2pw";
        String excelFilePath = "E:/FH/SS23/SWEN/exports/logs.xlsx";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT * FROM logs";
            ResultSet resultSet = statement.executeQuery(selectQuery);

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
            while (resultSet.next()) {
                Row dataRow = sheet.createRow(rowNumber);
                dataRow.createCell(0).setCellValue(resultSet.getInt("id"));
                dataRow.createCell(1).setCellValue(resultSet.getInt("tour_id"));
                dataRow.createCell(2).setCellValue(resultSet.getString("date_time"));
                dataRow.createCell(3).setCellValue(resultSet.getString("comment"));
                dataRow.createCell(4).setCellValue(resultSet.getString("difficulty"));
                dataRow.createCell(5).setCellValue(resultSet.getInt("total_time"));
                dataRow.createCell(6).setCellValue(resultSet.getInt("rating"));
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
