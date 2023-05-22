package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class NewTourView implements Initializable {

    @Autowired
    private TourService tourService;
    @Autowired
    private SearchView searchView;
    @Autowired
    private NewTourViewModel newTourViewModel;

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
    public TextField transportTypeTextField;


    @Override
    public void initialize(URL location, ResourceBundle rb) {
        nameTextField.textProperty().bindBidirectional(newTourViewModel.nameProperty());
        tourDescriptionTextField.textProperty().bindBidirectional(newTourViewModel.tourDescriptionProperty());
        tourFromTextField.textProperty().bindBidirectional(newTourViewModel.tourFromProperty());
        tourToTextField.textProperty().bindBidirectional(newTourViewModel.tourToProperty());
        transportTypeTextField.textProperty().bindBidirectional(newTourViewModel.transportTypeProperty());
    }

    public void submitButtonAction(ActionEvent event) throws IOException {
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
        if (tourToTextField.getText().isEmpty()) {
            feedbackText.setText("tour destination not entered!");
            return;
        }
        if (transportTypeTextField.getText().isEmpty()) {
            feedbackText.setText("transport type not entered!");
            return;
        }
//        if (transportTypeTextField.getText() != "fastest" && transportTypeTextField.getText() != "shortest" && transportTypeTextField.getText() != "pedestrian" && transportTypeTextField.getText() != "bicycle") {
//            feedbackText.setText("transport type has to be fastest, shortest, pedestrian or bicycle!");
//            return;
//        }

        newTourViewModel.addNewTour();
    }
}
