package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
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
public class NewTourView implements Initializable {

    private final NewTourViewModel newTourViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    private TextField nameTextField;
    @FXML
    public TextField tourFromTextField;
    @FXML
    public TextField tourDescriptionTextField;
    @FXML
    public TextField tourToTextField;
    @FXML
    public ComboBox<String> transportTypeComboBox;

    public NewTourView(NewTourViewModel newTourViewModel) {
        log.debug("new tour controller");
        this.newTourViewModel = newTourViewModel;
    }


    @Override
    public void initialize(URL location, ResourceBundle rb) {

        log.debug("Initializing new tour");

        List<String> transportTypeList = List.of("fastest", "shortest", "pedestrian", "bicycle");
        transportTypeComboBox.getItems().addAll(transportTypeList);

        nameTextField.textProperty().bindBidirectional(newTourViewModel.nameProperty());
        tourDescriptionTextField.textProperty().bindBidirectional(newTourViewModel.tourDescriptionProperty());
        tourFromTextField.textProperty().bindBidirectional(newTourViewModel.tourFromProperty());
        tourToTextField.textProperty().bindBidirectional(newTourViewModel.tourToProperty());
        transportTypeComboBox.valueProperty().bindBidirectional(newTourViewModel.transportTypeProperty());
    }

    public void submitButtonAction() {
        if (nameTextField.getText() == null) {
            System.out.println("name not entered!");
            feedbackText.setText("name not entered!");
            return;
        }
        if (tourDescriptionTextField.getText() == null) {
            feedbackText.setText("description not entered!");
            return;
        }
        if (tourFromTextField.getText() == null) {
            feedbackText.setText("tour start not entered!");
            return;
        }
        if (tourToTextField.getText() == null) {
            feedbackText.setText("tour destination not entered!");
            return;
        }
        if (transportTypeComboBox.getValue() == null) {
            feedbackText.setText("transport type not entered!");
            return;
        }

        newTourViewModel.addNewTour();
    }
}
