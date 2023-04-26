package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Person;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import ch.qos.logback.core.net.SyslogOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourListViewModel {

    @Autowired
    TourService tourService;
    @FXML
    private ListView listView;

    @FXML public void handleMouseClick() {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
    }

    private List<Tour> masterData = new ArrayList<>();
    private ObservableList<Tour> tourListItems = FXCollections.observableArrayList();

    public ObservableList<Tour> getTourListItems() {
        return tourListItems;
    }
    final ListView lv = new ListView(FXCollections.observableList(tourListItems));

    public void addItem(Tour tour) {
        tourListItems.add(tour);
        masterData.add(tour);
    }

    public void clearItems(){ tourListItems.clear(); }

    public void initList(){
        tourService.getTourList().forEach(p -> {
            System.out.println(p);
            addItem(p);
        });
    }
    /*tourListItems.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    System.out.println("Double clicked");
                }
            }
        }
    });*/

    /*lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("clicked on " + lv.getSelectionModel().getSelectedItem());
        }
    });*/
    public void filterList(String searchText){
        Task<List<Tour>> task = new Task<>() {
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

    }


}
