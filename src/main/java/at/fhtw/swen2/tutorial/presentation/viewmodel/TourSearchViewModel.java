package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TourSearchViewModel {

    @Autowired
    private TourListViewModel tourListViewModel;

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
        tourListViewModel.filterList(getSearchString());
    }



}
