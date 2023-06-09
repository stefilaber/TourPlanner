package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.Swen2TemplateApplication;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.PDFGeneratorService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@Component
@Scope("prototype")
@Slf4j
public class LogListView implements Initializable{

    public final LogListViewModel logListViewModel;
    private final Swen2TemplateApplication swen2TemplateApplication;
    private final PDFGeneratorService pdfGeneratorService;

    @FXML
    public TableView<Log> tableView = new TableView<>();
    @FXML
    private VBox dataContainer;
    @FXML
    private VBox mapContainer;
    @FXML
    private Button editLogButton;
    @FXML
    private Button saveEditedLogButton;
    @FXML
    private Button tourReportButton;


    public LogListView(LogListViewModel logListViewModel, Swen2TemplateApplication swen2TemplateApplication, PDFGeneratorService pdfGeneratorService) {
        log.debug("Initializing application controller");
        this.logListViewModel = logListViewModel;
        this.swen2TemplateApplication = swen2TemplateApplication;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){
        log.debug("Initializing application");
        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(600);
        imageView.imageProperty().bind(logListViewModel.mapProperty());

        saveEditedLogButton.setVisible(false);
        tableView.setItems(logListViewModel.getLogListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Log, String> dateTime = new TableColumn<>("DATE/TIME");
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<Log, String> comment = new TableColumn<>("COMMENT");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment.setCellFactory(TextFieldTableCell.forTableColumn());
        comment.setOnEditCommit(event -> {
            Log log = event.getRowValue();
            log.setComment(event.getNewValue());
        });

        TableColumn<Log, String> difficulty = new TableColumn<>("DIFFICULTY");
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficulty.setCellFactory(TextFieldTableCell.forTableColumn());
        difficulty.setOnEditCommit(event -> {
            Log log = event.getRowValue();
            log.setDifficulty(event.getNewValue().toLowerCase());
        });

        TableColumn<Log, String> totalTime = new TableColumn<>("TOTAL TIME");
        totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        totalTime.setCellFactory(TextFieldTableCell.forTableColumn());
        totalTime.setOnEditCommit(event -> {
            Log log = event.getRowValue();
            log.setTotalTime(event.getNewValue());
        });

        TableColumn<Log, Integer> rating = new TableColumn<>("RATING");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        rating.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        rating.setOnEditCommit(event -> {
            Log log = event.getRowValue();
            log.setRating(event.getNewValue());
        });

        var columns = tableView.getColumns();
        Stream.of(dateTime, comment, difficulty, totalTime, rating).forEach(columns::add);

        dataContainer.getChildren().add(tableView);
        mapContainer.getChildren().add(imageView);

        tourReportButton.setOnAction(event -> {
            logListViewModel.generateTourReport();
        });

        log.debug("Application initialized");
    }

    public void deleteButtonAction() {

        log.info("delete button clicked");
        logListViewModel.deleteLog(tableView.getSelectionModel().getSelectedItem());
    }

    public void editButtonAction() {

        log.info("edit tour button clicked");
        //make table editable
        tableView.setEditable(true);

        editLogButton.setVisible(false);
        saveEditedLogButton.setVisible(true);

    }

    public void saveEditedLogButtonAction(ActionEvent actionEvent) {

        log.info("update tour button clicked");

        Log selectedLog = tableView.getSelectionModel().getSelectedItem();
        logListViewModel.saveEditedLog(selectedLog);

        //make table uneditable
        tableView.setEditable(false);

        editLogButton.setVisible(true);
        saveEditedLogButton.setVisible(false);
    }
}
