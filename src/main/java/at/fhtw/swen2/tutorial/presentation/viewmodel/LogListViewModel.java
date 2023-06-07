package at.fhtw.swen2.tutorial.presentation.viewmodel;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class LogListViewModel {

    @Autowired
    LogService logService;
    @FXML
    private ListView listView;

    public Consumer<Log> onLogDoubleClick = log -> {};
    private Long selectedTourId = Long.valueOf(0);

    private List<Log> masterData = new ArrayList<>();
    private ObservableList<Log> logListItems = FXCollections.observableArrayList();

    public ObservableList<Log> getLogListItems() {
        return logListItems;
    }
    final ListView lv = new ListView(FXCollections.observableList(logListItems));

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
}
