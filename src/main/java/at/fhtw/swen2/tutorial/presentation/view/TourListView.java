package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class TourListView implements Initializable{

    @Autowired
    public TourListViewModel tourListViewModel;

    @FXML
    public TableView tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(tourListViewModel.getTourListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn name = new TableColumn("NAME");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn tourDescription = new TableColumn("TOUR DESCRIPTION");
        tourDescription.setCellValueFactory(new PropertyValueFactory("tourDescription"));
        TableColumn tourFrom = new TableColumn("FROM");
        tourFrom.setCellValueFactory(new PropertyValueFactory("tourFrom"));
        TableColumn tourTo = new TableColumn("TO");
        tourTo.setCellValueFactory(new PropertyValueFactory("tourTo"));
        TableColumn transportType = new TableColumn("TRANSPORT TYPE");
        transportType.setCellValueFactory(new PropertyValueFactory("transportType"));
        TableColumn tourDistance = new TableColumn("TOUR DISTANCE");
        tourDistance.setCellValueFactory(new PropertyValueFactory("tourDistance"));
        TableColumn estimatedTime = new TableColumn("ESTIMATED TIME");
        estimatedTime.setCellValueFactory(new PropertyValueFactory("estimatedTime"));

        tableView.getColumns().addAll(id, name, tourDescription, tourFrom, tourTo, transportType, tourDistance, estimatedTime);


        dataContainer.getChildren().add(tableView);
        tourListViewModel.initList();
    }

}
