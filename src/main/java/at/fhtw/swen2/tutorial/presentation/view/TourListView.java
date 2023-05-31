package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component
@Scope("prototype")
@Slf4j
public class TourListView implements Initializable{

    private final TourListViewModel tourListViewModel;

    @FXML
    public TableView<Tour> tableView = new TableView<>();
    @FXML
    private VBox dataContainer;
    @FXML
    private Button editTourButton;

    @FXML
    private Button saveEditedTourButton;

    public TourListView(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){

        saveEditedTourButton.setVisible(false);

        tableView.setItems(tourListViewModel.getTourListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tour, String> name = new TableColumn<>("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Tour, String> tourDescription = new TableColumn<>("DESCRIPTION");
        tourDescription.setCellValueFactory(new PropertyValueFactory<>("tourDescription"));
        tourDescription.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Tour, String> tourFrom = new TableColumn<>("FROM");
        tourFrom.setCellValueFactory(new PropertyValueFactory<>("tourFrom"));
        tourFrom.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Tour, String> tourTo = new TableColumn<>("TO");
        tourTo.setCellValueFactory(new PropertyValueFactory<>("tourTo"));
        tourTo.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Tour, String> transportType = new TableColumn<>("TRANSPORT TYPE");
        transportType.setCellValueFactory(new PropertyValueFactory<>("transportType"));
        transportType.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Tour, Integer> tourDistance = new TableColumn<>("DISTANCE");
        tourDistance.setCellValueFactory(new PropertyValueFactory<>("tourDistance"));

        TableColumn<Tour, Integer> estimatedTime = new TableColumn<>("DURATION");
        estimatedTime.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));

        var columns = tableView.getColumns();
        Stream.of(name, tourDescription, tourFrom, tourTo, transportType, tourDistance, estimatedTime).forEach(columns::add);



        tableView.setRowFactory( tv -> {
            TableRow<Tour> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Tour selectedTour = tableView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
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
    }

    public void editButtonAction(ActionEvent actionEvent) {

        tableView.setEditable(true);

        editTourButton.setVisible(false);
        saveEditedTourButton.setVisible(true);
    }

    public void saveEditedTourButtonAction(ActionEvent actionEvent) {

        tableView.setEditable(false);

        editTourButton.setVisible(true);
        saveEditedTourButton.setVisible(false);
    }
}
