package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static at.fhtw.swen2.tutorial.service.PDFGeneratorService.*;

@Component
@Scope("prototype")
public class LogListView implements Initializable{

    public final LogListViewModel logListViewModel;

    @FXML
    public TableView<Log> tableView = new TableView<>();
    @FXML
    public Button tourReportButton;
    @FXML
    private VBox dataContainer;

    public LogListView(LogListViewModel logListViewModel) {
        this.logListViewModel = logListViewModel;
    }

    public static final String TOUR_REPORT = "target/reports/tourReport.pdf";
    public static final File tourReportFile = new File(TOUR_REPORT);

    @Override
    public void initialize(URL location, ResourceBundle rb){

        tourReportButton.setOnAction(event -> {
            try {
                System.out.println("Generating tour report...");
                fileExists(tourReportFile);
                writeTourReport(generateReport(TOUR_REPORT));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
        //logListViewModel.initList();

    }

}
