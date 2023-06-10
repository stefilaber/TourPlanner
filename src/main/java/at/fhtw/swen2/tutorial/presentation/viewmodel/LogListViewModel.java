package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.*;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class LogListViewModel {

    @FXML
    private ListView listView;
    
    final LogService logService;
    final TourService tourService;
    final ImportDataService importLogsService;
    private final ExportDataService exportLogsService;
    final PDFGeneratorService pdfGeneratorService;

    public Consumer<Log> onLogDoubleClick = log -> {};
    private Long selectedTourId = Long.valueOf(0);

    private List<Log> masterData = new ArrayList<>();
    private ObservableList<Log> logListItems = FXCollections.observableArrayList();

    private final ObjectProperty<Image> map = new SimpleObjectProperty<>();

    public LogListViewModel(LogService logService, @Qualifier("importLogsServiceImpl") ImportDataService importLogsService, @Qualifier("exportLogsServiceImpl") ExportDataService exportLogsService, PDFGeneratorService pdfGeneratorService, TourService tourService) {
        this.logService = logService;
        this.importLogsService = importLogsService;
        this.exportLogsService = exportLogsService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.tourService = tourService;
    }

    public Image getMap() {
        return map.get();
    }

    public void setMap(Image map) {
        this.map.set(map);
    }

    public ObjectProperty<Image> mapProperty() {
        return map;
    }
    
    public ObservableList<Log> getLogListItems() {
        return logListItems;
    }

    public void addItem(Log log) {
        logListItems.add(log);
        masterData.add(log);
    }
    
    public void clearItems(){ logListItems.clear(); }

    public void initList(){
        clearItems();
        logService.getLogList(selectedTourId).forEach(p -> {
            System.out.println(p);
            addItem(p);
        });
    }

    public void setSelectedTourId(Long selectedTourId) {
        this.selectedTourId = selectedTourId;
    }

    public Long getSelectedTourId() {
        return selectedTourId;
    }

    public void deleteLog(Log log){
        logService.delete(log);
        logListItems.remove(log);
        masterData.remove(log);
    }

    public void saveEditedLog(Log log) {
        logService.save(log);
        logListItems.setAll(masterData);
    }

    public void importLogs(File file) throws Exception {
        List<Log> logs= importLogsService.importData(file);
        for (Log log : logs) {
            System.out.println(log);
            logService.save(log);
            addItem(log);
        }
    }

    public void exportLogs() throws Exception {
        exportLogsService.exportData();
    }

    public void filterList(String searchText) {
        Task<List<Log>> task = new Task<>() {
            @Override
            protected List<Log> call() {
                updateMessage("Loading data");
                return masterData
                        .stream()
                        .filter(value -> value.getDateTime().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getComment().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getDifficulty().toLowerCase().contains(searchText.toLowerCase()) ||
                                checkTimeSpan(value.getTotalTime(), searchText) ||
                                String.valueOf(value.getRating()).toLowerCase().contains(searchText.toLowerCase())
                        )
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> logListItems.setAll(task.getValue()));

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private int convertToSeconds(String time){
        String[] timeArray = time.split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = 0;
        if(timeArray.length >= 2) {
            minutes = Integer.parseInt(timeArray[1]);
        }
        int seconds = 0;
        //check if there are seconds in the time string
        if(timeArray.length == 3){
            seconds = Integer.parseInt(timeArray[2]);
        }

        return hours*3600 + minutes*60 + seconds;
    }

    private boolean checkTimeSpan(String logTime, String searchTime){

        //accepts time spans of 30 minutes (+- 1800 seconds of the tourSeconds)
        try {
            return convertToSeconds(searchTime) >= (convertToSeconds(logTime) - 1800) && convertToSeconds(searchTime) <= convertToSeconds(logTime) + 1800;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void generateTourReport(){

        try {
            Tour tour = tourService.getTour(selectedTourId);
            String TOUR_REPORT = "target/reports/" + tour.getName() + "TourReport.pdf";
            File tourReportFile = new File(TOUR_REPORT);
            System.out.println("Generating tour report...");
            pdfGeneratorService.fileExists(tourReportFile);
            pdfGeneratorService.writeTourReport(TOUR_REPORT, tour);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
