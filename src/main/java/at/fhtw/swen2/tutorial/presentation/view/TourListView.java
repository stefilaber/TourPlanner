package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.service.ImportDataService;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.impl.LogServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component
@Scope("prototype")
@Slf4j
public class TourListView implements Initializable{

    private final ImportDataService importLogsService;
    private final TourListViewModel tourListViewModel;

    @FXML
    public TableView<Tour> tableView = new TableView<>();
    @FXML
    private StackPane dataContainer;

    @FXML
    private Button editTourButton;

    @FXML
    private Button saveEditedTourButton;

    private String selectedTourName;

    public TourListView(TourListViewModel tourListViewModel, ImportDataService importLogsService) {
        this.tourListViewModel = tourListViewModel;
        this.importLogsService = importLogsService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){

        saveEditedTourButton.setVisible(false);

        tableView.setItems(tourListViewModel.getTourListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tour, String> name = new TableColumn<>("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> {
            Tour tour = event.getRowValue();
            tour.setName(event.getNewValue());
        });

        TableColumn<Tour, String> tourDescription = new TableColumn<>("DESCRIPTION");
        tourDescription.setCellValueFactory(new PropertyValueFactory<>("tourDescription"));
        tourDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        tourDescription.setOnEditCommit(event -> {
            Tour tour = event.getRowValue();
            tour.setTourDescription(event.getNewValue());
        });

        TableColumn<Tour, String> tourFrom = new TableColumn<>("FROM");
        tourFrom.setCellValueFactory(new PropertyValueFactory<>("tourFrom"));
        tourFrom.setCellFactory(TextFieldTableCell.forTableColumn());
        tourFrom.setOnEditCommit(event -> {
            Tour tour = event.getRowValue();
            tour.setTourFrom(event.getNewValue());
        });

        TableColumn<Tour, String> tourTo = new TableColumn<>("TO");
        tourTo.setCellValueFactory(new PropertyValueFactory<>("tourTo"));
        tourTo.setCellFactory(TextFieldTableCell.forTableColumn());
        tourTo.setOnEditCommit(event -> {
            Tour tour = event.getRowValue();
            tour.setTourTo(event.getNewValue());
        });

        TableColumn<Tour, String> transportType = new TableColumn<>("TRANSPORT TYPE");
        transportType.setCellValueFactory(new PropertyValueFactory<>("transportType"));
        transportType.setCellFactory(TextFieldTableCell.forTableColumn());
        transportType.setOnEditCommit(event -> {
            Tour tour = event.getRowValue();
            tour.setTransportType(event.getNewValue());
        });

        TableColumn<Tour, Integer> tourDistance = new TableColumn<>("DISTANCE");
        tourDistance.setCellValueFactory(new PropertyValueFactory<>("tourDistance"));

        TableColumn<Tour, String> estimatedTime = new TableColumn<>("DURATION");
        estimatedTime.setCellValueFactory(cellData -> new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.ofSecondOfDay(cellData.getValue().getEstimatedTime()))));

        var columns = tableView.getColumns();
        Stream.of(name, tourDescription, tourFrom, tourTo, transportType, tourDistance, estimatedTime).forEach(columns::add);

        tableView.setRowFactory( tv -> {
            TableRow<Tour> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                //to get selected tour name:
                if (event.getClickCount() == 1) {
                    Tour rowData = row.getItem();
                    selectedTourName = rowData.getName();

                //to redirect to logs tab and show logs for selected tour:
                }
                if (event.getClickCount() == 2 && (! row.isEmpty()) && editTourButton.isVisible()) {
                    Tour rowData = row.getItem();
                    tourListViewModel.onTourDoubleClick.accept(rowData);

                }
            });
            return row ;
        });

        dataContainer.getChildren().add(tableView);
        tourListViewModel.initList();

    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        Tour selectedTour = tableView.getSelectionModel().getSelectedItem();
        tourListViewModel.deleteTour(selectedTour);
        tourListViewModel.deleteMap(selectedTourName);
    }

    public void editButtonAction(ActionEvent actionEvent) {

        //make table editable
        tableView.setEditable(true);

        editTourButton.setVisible(false);
        saveEditedTourButton.setVisible(true);
    }

    public void saveEditedTourButtonAction(ActionEvent actionEvent) {

        Tour selectedTour = tableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedTour);

        tourListViewModel.deleteMap(selectedTourName);
        tourListViewModel.saveEditedTour(selectedTour);

        //make table uneditable
        tableView.setEditable(false);

        editTourButton.setVisible(true);
        saveEditedTourButton.setVisible(false);
    }

    public void importLogsButtonAction(ActionEvent actionEvent) throws Exception {
        String path = "E:/FH/SS23/SWEN/imports/logs.xlsx";
        importLogsService.importLogs(path);
    }
}

