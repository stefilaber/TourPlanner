package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.ExportDataService;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class LogListViewModel {

    final
    LogService logService;
    @FXML
    private ListView listView;

    final ImportDataService importLogsService;

    private final ExportDataService exportLogsService;

    public Consumer<Log> onLogDoubleClick = log -> {};
    private Long selectedTourId = Long.valueOf(0);

    private List<Log> masterData = new ArrayList<>();
    private ObservableList<Log> logListItems = FXCollections.observableArrayList();

    public LogListViewModel(LogService logService, @Qualifier("importLogsServiceImpl") ImportDataService importLogsService, @Qualifier("exportLogsServiceImpl") ExportDataService exportLogsService) {
        this.logService = logService;
        this.importLogsService = importLogsService;
        this.exportLogsService = exportLogsService;
    }


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

    public void importLogs(File file) throws Exception {
        List<Log> logs= importLogsService.importData(file);
        for (Log log : logs) {
            addItem(log);
        }
    }

    public void exportLogs() throws Exception {
        exportLogsService.exportData();
    }

}
