package at.fhtw.swen2.tutorial.service.impl;
import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.mapper.TourMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class ImportToursServiceImpl implements ImportDataService<Tour> {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    public ImportToursServiceImpl(TourRepository tourRepository, TourMapper tourMapper) {
        log.debug("ImportToursServiceImpl created");
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
    }

    public List<Tour> importData(File file) throws IOException {

        List<Tour> data = new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Skip header row
            if (row.getRowNum() == 0) {
                continue;
            }

            Long id = (long) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String tourDescription = row.getCell(2).getStringCellValue();
            String tourFrom = row.getCell(3).getStringCellValue();
            String tourTo = row.getCell(4).getStringCellValue();
            String transportType = row.getCell(5).getStringCellValue();
            int tourDistance = (int) row.getCell(6).getNumericCellValue();
            int estimatedTime = (int) row.getCell(7).getNumericCellValue();

            TourEntity tour = TourEntity.builder()
                    .id(id)
                    .name(name)
                    .tourDescription(tourDescription)
                    .tourFrom(tourFrom)
                    .tourTo(tourTo)
                    .transportType(transportType)
                    .tourDistance(tourDistance)
                    .estimatedTime(estimatedTime)
                    .build();
            tourRepository.save(tour);
            log.info("Record inserted for ID: " + id);
            data.add(tourMapper.fromEntity(tour));
        }

        workbook.close();
        fileInputStream.close();

        log.info("Data imported successfully.");

        return data;
    }
}

