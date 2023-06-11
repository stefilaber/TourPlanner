package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class NewLogView implements Initializable {

    private final NewLogViewModel newLogViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    public TextField commentTextField;
    @FXML
    public ComboBox<String> difficultyComboBox;
    @FXML
    public TextField totalTimeTextField;
    @FXML
    public ComboBox<Integer> ratingComboBox;
    public NewLogView(NewLogViewModel newLogViewModel) {
        this.newLogViewModel = newLogViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {

        log.debug("Initializing new log view");

        List <String> difficultyList = List.of("easy", "medium", "hard");
        difficultyComboBox.getItems().addAll(difficultyList);

        List<Integer> ratingList = List.of(1, 2, 3, 4, 5);
        ratingComboBox.getItems().addAll(ratingList);

        commentTextField.textProperty().bindBidirectional(newLogViewModel.commentProperty());
        difficultyComboBox.valueProperty().bindBidirectional(newLogViewModel.difficultyProperty());
        totalTimeTextField.textProperty().bindBidirectional(newLogViewModel.totalTimeProperty());
        ratingComboBox.valueProperty().bindBidirectional(newLogViewModel.ratingProperty().asObject());
    }


    public void submitButtonAction() {
        if (commentTextField.getText() == null) {
            log.debug("comment not entered");
            feedbackText.setText("comment not entered!");
            return;
        }
        if (difficultyComboBox.getValue() == null) {
            log.debug("difficulty not entered");
            feedbackText.setText("difficulty not entered!");
            return;
        }
        if (totalTimeTextField.getText() == null) {
            log.debug("total time not entered");
            feedbackText.setText("total time not entered!");
            return;
        }
        String timeRegex = "[0-9]{1,2}";
        if(!(totalTimeTextField.getText().matches(timeRegex) || totalTimeTextField.getText().matches(timeRegex + ":" + timeRegex) || totalTimeTextField.getText().matches(timeRegex + ":" + timeRegex + ":" + timeRegex))) {
            log.debug("total time is not a valid time");
            feedbackText.setText("total time is not a valid time!");
            return;
        }
        if (ratingComboBox.getValue() == null || ratingComboBox.getValue() == 0) {
            log.debug("rating not entered");
            feedbackText.setText("rating not entered!");
            return;
        }

        newLogViewModel.setRating(ratingComboBox.getValue());
        newLogViewModel.addNewLog();
    }
}
