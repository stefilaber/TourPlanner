package at.fhtw.swen2.tutorial.presentation.view;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogSearchViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Component
@Scope("prototype")
@Slf4j
public class LogSearchView {

    private final LogSearchViewModel logSearchViewModel;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private static Logger logger = LogManager.getLogger(LogSearchView.class);


    public LogSearchView(LogSearchViewModel logSearchViewModel) {
        logger.debug("logSearchView controller");
        this.logSearchViewModel = logSearchViewModel;
    }

    @FXML
    private void initialize() {

        logger.debug("Initializing log search view");

        searchField.textProperty().bindBidirectional(logSearchViewModel.searchStringProperty());

        // search panel
        searchButton.setText("Search");
        searchButton.setOnAction(event -> loadData());
        searchButton.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loadData();
            }
        });
    }

    private void loadData() {
        logger.debug("Loading filtered data");
        logSearchViewModel.search();
    }

}
