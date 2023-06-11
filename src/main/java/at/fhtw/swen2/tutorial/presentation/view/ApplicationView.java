package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.StageAware;
import at.fhtw.swen2.tutorial.presentation.events.ApplicationShutdownEvent;
import at.fhtw.swen2.tutorial.presentation.viewmodel.LogListViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewLogViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class ApplicationView implements Initializable, StageAware {

    @FXML
    public Tab logTab;
    @FXML
    public TabPane tabPane;
    ApplicationEventPublisher publisher;
    @FXML BorderPane layout;
    // Menu, at some point break out
    @FXML MenuItem miPreferences;
    @FXML MenuItem miQuit;
    @FXML MenuItem miAbout;

    // Toolbar, at some point break out
    @FXML Label tbMonitorStatus;
    Circle monitorStatusIcon = new Circle(8);

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();

    private static Logger logger = LogManager.getLogger(ApplicationView.class);

    public ApplicationView(ApplicationEventPublisher publisher, LogListViewModel logListViewModel, TourListViewModel tourListViewModel, NewLogViewModel newLogViewModel) {
        logger.debug("Initializing application controller");
        this.publisher = publisher;

        tourListViewModel.onTourDoubleClick = tour -> {
            logger.info("Tour double clicked: {}", tour);
            logTab.setDisable(false);
            logListViewModel.setSelectedTourId(tour.getId());
            newLogViewModel.setSelectedTourId(tour.getId());
            logListViewModel.initList();
            tabPane.getSelectionModel().select(logTab);
            try {
                Image image = new Image(getClass().getResourceAsStream("/maps/" + tour.getName() + ".png"));
                logListViewModel.setMap(image);
            } catch (Exception e) {
                logger.error("Could not load map for tour: {}", tour.getName());
            }
            logger.info("Got map from path: {}", getClass().getResourceAsStream("/maps/" + tour.getName() + ".png"));

        };
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));
        tbMonitorStatus.setGraphic(monitorStatusIcon);
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        publisher.publishEvent(new ApplicationShutdownEvent(event.getSource()));
    }

    @FXML
    public void onHelpAbout() {
        new AboutDialogView().show();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }
}
