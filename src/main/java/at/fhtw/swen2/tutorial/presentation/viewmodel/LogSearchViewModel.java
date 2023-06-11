package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.presentation.view.ApplicationView;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Component
public class LogSearchViewModel {

    @Autowired
    private LogListViewModel logListViewModel;

    private static Logger logger = LogManager.getLogger(LogSearchViewModel.class);

    private SimpleStringProperty searchString = new SimpleStringProperty();


    public String getSearchString() {
        return searchString.get();
    }

    public SimpleStringProperty searchStringProperty() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString.set(searchString);
    }

    public void search() {
        logListViewModel.filterList(getSearchString());

    }
}
