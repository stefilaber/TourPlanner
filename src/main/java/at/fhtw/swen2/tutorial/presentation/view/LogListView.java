package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
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
public class LogListView implements Initializable{

    @Autowired
    public LogListViewModel logListViewModel;

    @FXML
    public TableView tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(logListViewModel.getLogListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn dateTime = new TableColumn("DATE/TIME");
        dateTime.setCellValueFactory(new PropertyValueFactory("dateTime"));
        TableColumn comment = new TableColumn("COMMENT");
        comment.setCellValueFactory(new PropertyValueFactory("comment"));
        TableColumn difficulty = new TableColumn("DIFFICULTY");
        difficulty.setCellValueFactory(new PropertyValueFactory("difficulty"));
        TableColumn totalTime = new TableColumn("TOTAL TIME");
        totalTime.setCellValueFactory(new PropertyValueFactory("totalTime"));
        TableColumn rating = new TableColumn("RATING");
        rating.setCellValueFactory(new PropertyValueFactory("rating"));

        tableView.getColumns().addAll(id, dateTime, comment, difficulty, totalTime, rating);


        dataContainer.getChildren().add(tableView);
        //logListViewModel.initList();


    }

}
