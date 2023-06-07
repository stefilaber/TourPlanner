package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component
@Scope("prototype")
public class LogListView implements Initializable{

    public final LogListViewModel logListViewModel;

    @FXML
    public TableView<Log> tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    @FXML
    private VBox mapContainer;

    public LogListView(LogListViewModel logListViewModel) {
        this.logListViewModel = logListViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){

        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(600);
        imageView.imageProperty().bind(logListViewModel.mapProperty());

        tableView.setItems(logListViewModel.getLogListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Log, String> dateTime = new TableColumn<>("DATE/TIME");
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        TableColumn<Log, String> comment = new TableColumn<>("COMMENT");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        TableColumn<Log, String> difficulty = new TableColumn<>("DIFFICULTY");
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        TableColumn<Log, Integer> totalTime = new TableColumn<>("TOTAL TIME");
        totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        TableColumn<Log, Integer> rating = new TableColumn<>("RATING");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        var columns = tableView.getColumns();
        Stream.of(dateTime, comment, difficulty, totalTime, rating).forEach(columns::add);


        dataContainer.getChildren().add(tableView);
        mapContainer.getChildren().add(imageView);


    }

}
