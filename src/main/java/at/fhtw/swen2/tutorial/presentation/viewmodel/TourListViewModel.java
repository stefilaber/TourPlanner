package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.ExportDataService;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.PDFGeneratorService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class TourListViewModel {

    final TourService tourService;
    final ImportDataService importToursService;
    private final ExportDataService exportToursService;

    private final PDFGeneratorService pdfGeneratorService;

    public Consumer<Tour> onTourDoubleClick = tour -> {};

    private final List<Tour> masterData = new ArrayList<>();
    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();

    public TourListViewModel(TourService tourService, @Qualifier("importToursServiceImpl") ImportDataService importToursService, @Qualifier("exportToursServiceImpl") ExportDataService exportToursService, PDFGeneratorService pdfGeneratorService) {
        this.tourService = tourService;
        this.importToursService = importToursService;
        this.exportToursService = exportToursService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public ObservableList<Tour> getTourListItems() {
        return tourListItems;
    }

    public void addItem(Tour tour) {
        tourListItems.add(tour);
        masterData.add(tour);
    }

    public void initList(){
        tourService.getTourList().forEach(p -> {
            System.out.println(p);
            addItem(p);
        });
    }

    public void filterList(String searchText){
        Task<List<Tour>> task = new Task<>() {
            @Override
            protected List<Tour> call() {
                updateMessage("Loading data");
                return masterData
                        .stream()
                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getTourDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getTourFrom().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getTourTo().toLowerCase().contains(searchText.toLowerCase()) ||
                                value.getTransportType().toLowerCase().contains(searchText.toLowerCase()) ||
                                checkDistance(value.getTourDistance(), searchText) ||
                                checkTimeSpan(value.getEstimatedTime(), searchText)
                        )
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> tourListItems.setAll(task.getValue()));

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    public void deleteTour(Tour selectedTour) {

        tourService.delete(selectedTour);
        tourListItems.remove(selectedTour);
        masterData.remove(selectedTour);
    }

    public void saveEditedTour(Tour tour){

        try {
            tourService.save(tour);
            tourListItems.setAll(masterData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMap(String name){
        tourService.deleteMap(name);
    }

    public void importTours(File file) throws Exception {
        List<Tour> tours = importToursService.importData(file);
        for (Tour tour : tours) {
            addItem(tour);
        }
    }

    public void exportTours() throws Exception {
        exportToursService.exportData();
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

    private boolean checkTimeSpan(int tourSeconds, String searchTime){

        //accepts time spans of 30 minutes (+- 1800 seconds of the tourSeconds)
        try {
            System.out.println(convertToSeconds(searchTime) >= (tourSeconds - 1800) && convertToSeconds(searchTime) <= tourSeconds + 1800);
            return convertToSeconds(searchTime) >= (tourSeconds - 1800) && convertToSeconds(searchTime) <= tourSeconds + 1800;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private boolean checkDistance(int tourDistance, String searchDistance){
        try {
            return Integer.parseInt(searchDistance) >= (tourDistance - 20) && Integer.parseInt(searchDistance) <= tourDistance + 20;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public void generateSummaryReport(){
        String SUMMARY_REPORT = "target/reports/summaryReport.pdf";
        File summaryReportFile = new File(SUMMARY_REPORT);
        try {
            System.out.println("Generating summary report...");
            pdfGeneratorService.fileExists(summaryReportFile);
            pdfGeneratorService.writeSummaryReport(SUMMARY_REPORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
