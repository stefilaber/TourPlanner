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

    public void editLog(Log log) {
        logService.edit(log);
        logListItems.set(logListItems.indexOf(log), log);
        masterData.set(masterData.indexOf(log), log);
    }
}
