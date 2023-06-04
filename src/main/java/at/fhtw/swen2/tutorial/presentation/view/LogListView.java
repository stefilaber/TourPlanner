package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
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

    public LogListView(LogListViewModel logListViewModel) {
        this.logListViewModel = logListViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(logListViewModel.getLogListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Log, String> dateTime = new TableColumn<>("DATE/TIME");
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<Log, String> comment = new TableColumn<>("COMMENT");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        comment.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Log, String> difficulty = new TableColumn<>("DIFFICULTY");
        difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficulty.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Log, Integer> totalTime = new TableColumn<>("TOTAL TIME");
        totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        totalTime.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        TableColumn<Log, Integer> rating = new TableColumn<>("RATING");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        rating.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        var columns = tableView.getColumns();
        Stream.of(dateTime, comment, difficulty, totalTime, rating).forEach(columns::add);

        tableView.setRowFactory( tv -> {
            TableRow<Log> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Log rowData = row.getItem();
                    tableView.setEditable(true);
                    logListViewModel.onLogDoubleClick.accept(rowData);
                }
            });
            return row;
        });

        dataContainer.getChildren().add(tableView);
        //logListViewModel.initList();

    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        logListViewModel.deleteLog(tableView.getSelectionModel().getSelectedItem());
    }

    public void editButtonAction(ActionEvent actionEvent) {
        logListViewModel.editLog(tableView.getSelectionModel().getSelectedItem());
    }
}
