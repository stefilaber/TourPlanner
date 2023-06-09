package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.ExportDataService;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.impl.ImportLogsServiceImpl;
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

    public Consumer<Tour> onTourDoubleClick = tour -> {};

    private final List<Tour> masterData = new ArrayList<>();
    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();

    public TourListViewModel(TourService tourService, @Qualifier("importToursServiceImpl") ImportDataService importToursService, @Qualifier("exportToursServiceImpl") ExportDataService exportToursService) {
        this.tourService = tourService;
        this.importToursService = importToursService;
        this.exportToursService = exportToursService;
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
                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase()))
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
}
