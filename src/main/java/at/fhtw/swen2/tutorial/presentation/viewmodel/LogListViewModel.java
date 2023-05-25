package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogListViewModel {

    @Autowired
    LogService logService;
    @FXML
    private ListView listView;

    private Long selectedTourId = Long.valueOf(0);

    private List<Log> masterData = new ArrayList<>();
    private ObservableList<Log> logListItems = FXCollections.observableArrayList();

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
    /*
    public void filterList(String searchText){
        Task<List<Log>> task = new Task<>() {
            @Override
            protected List<Tour> call() throws Exception {
                updateMessage("Loading data");
                return masterData
                        .stream()
                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> {
            tourListItems.setAll(task.getValue());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }*/

    public void setSelectedTourId(Long selectedTourId) {
        this.selectedTourId = selectedTourId;
    }

    public Long getSelectedTourId() {
        return selectedTourId;
    }

}
