package at.fhtw.swen2.tutorial.presentation;

import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LogListViewModelTests {

    private LogListViewModel logListViewModel;

    @Mock
    private LogService logService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        logListViewModel = new LogListViewModel(logService, null, null, null, null);
    }

    @Test
    public void addItemAddsLogToList() {
        // Arrange
        Log log = new Log(1L, 1L, "01/01/2023 - 10:10:10", "Test", "easy", "11:11", 1);

        // Act
        logListViewModel.addItem(log);

        // Assert
        ObservableList<Log> logListItems = logListViewModel.getLogListItems();
        assertEquals(1, logListItems.size());
        assertEquals(log, logListItems.get(0));
    }

    @Test
    public void clearItemsClearsLogList() {
        // Arrange
        Log log = new Log(1L, 1L, "01/01/2023 - 10:10:10", "Test", "easy", "11:11", 1);
        logListViewModel.addItem(log);

        // Act
        logListViewModel.clearItems();

        // Assert
        ObservableList<Log> logListItems = logListViewModel.getLogListItems();
        assertEquals(0, logListItems.size());
    }

//    @Test
//    public void initListRetrievesLogsFromService() {
//        // Arrange
//        List<Log> logList = new ArrayList<>();
//        logList.add(new Log(1L, 1L, "01/01/2023 - 10:10:10", "Test", "easy", "11:11", 1));
//        logList.add(new Log(2L, 1L, "02/02/2023 - 20:20:20", "Test2", "medium", "22:22", 2));
//        System.out.println(logList);
//        when(logService.getLogList(1L)).thenReturn(logList);
//
//        // Act
//        logListViewModel.setSelectedTourId(1L);
//        logListViewModel.initList();
//
//        // Assert
//        ObservableList<Log> logListItems = logListViewModel.getLogListItems();
//        assertEquals(2, logListItems.size());
//        verify(logService).getLogList(1L);
//    }
//
//    @Test
//    public void deleteLogDeletesLogFromListAndService() {
//        // Arrange
//        Log log = new Log(1L, 1L, "01/01/2023 - 10:10:10", "Test", "easy", "11:11", 1);
//        logListViewModel.getLogListItems().add(log);
//
//        // Act
//        logListViewModel.deleteLog(log);
//
//        // Assert
//        ObservableList<Log> logListItems = logListViewModel.getLogListItems();
//        assertEquals(0, logListItems.size());
//        verify(logService).delete(log);
//    }
//
//    @Test
//    public void saveEditedLogSavesLogUsingService() {
//        // Arrange
//        Log log = new Log(1L, 1L, "01/01/2023 - 10:10:10", "Test", "easy", "11:11", 1);
//        logListViewModel.getLogListItems().add(log);
//
//        // Act
//        logListViewModel.saveEditedLog(log);
//
//        // Assert
//        verify(logService).save(log);
//    }

}

