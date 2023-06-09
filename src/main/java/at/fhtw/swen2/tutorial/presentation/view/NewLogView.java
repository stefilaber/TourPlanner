package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewLogViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class NewLogView implements Initializable {

    @Autowired
    private LogService logService;
    @Autowired
    private NewLogViewModel newLogViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    private TextField dateTimeTextField;
    @FXML
    public TextField commentTextField;
    @FXML
    public TextField difficultyTextField;
    @FXML
    public TextField totalTimeTextField;
    @FXML
    public TextField ratingTextField;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        commentTextField.textProperty().bindBidirectional(newLogViewModel.commentProperty());
        difficultyTextField.textProperty().bindBidirectional(newLogViewModel.difficultyProperty());
        totalTimeTextField.textProperty().bindBidirectional(newLogViewModel.totalTimeProperty(), new NumberStringConverter());
        ratingTextField.textProperty().bindBidirectional(newLogViewModel.ratingProperty(), new NumberStringConverter());
        totalTimeTextField.textProperty().bindBidirectional(newLogViewModel.totalTimeProperty(), new NumberStringConverter());
    }

    public void submitButtonAction(ActionEvent event) {
        if (commentTextField.getText().isEmpty()) {
            feedbackText.setText("comment not entered!");
            return;
        }
        if (difficultyTextField.getText().isEmpty()) {
            feedbackText.setText("difficulty not entered!");
            return;
        }
        if (totalTimeTextField.getText().isEmpty()) {
            feedbackText.setText("total time not entered!");
            return;
        }
        if (ratingTextField.getText().isEmpty()) {
            feedbackText.setText("rating not entered!");
            return;
        }

        newLogViewModel.addNewLog();
    }
}
