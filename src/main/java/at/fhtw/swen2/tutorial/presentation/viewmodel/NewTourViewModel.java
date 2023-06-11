package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.MapQuestApiService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.beans.property.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;


@Component
public class NewTourViewModel {

    private static Logger logger = LogManager.getLogger(NewTourViewModel.class);
    private SimpleLongProperty id = new SimpleLongProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty tourDescription  = new SimpleStringProperty();
    private SimpleStringProperty tourFrom  = new SimpleStringProperty();
    private SimpleStringProperty tourTo  = new SimpleStringProperty();
    private SimpleStringProperty transportType  = new SimpleStringProperty();

    @Autowired
    private TourService tourService;
    @Autowired
    private TourListViewModel tourListViewModel;

    private Tour tour;

    public NewTourViewModel() { }

    public NewTourViewModel(Tour tour) {
        logger.debug("Initializing new tour view model with tour {}", tour);
        this.tour = tour;
        this.id = new SimpleLongProperty(tour.getId());
        this.name = new SimpleStringProperty(tour.getName());
        this.tourDescription = new SimpleStringProperty(tour.getTourDescription());
        this.tourFrom = new SimpleStringProperty(tour.getTourFrom());
        this.tourTo = new SimpleStringProperty(tour.getTourTo());
        this.transportType = new SimpleStringProperty(tour.getTransportType());
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTourDescription() { return tourDescription.get(); }

    public SimpleStringProperty tourDescriptionProperty() { return tourDescription; }

    public void setTourDescription(String tourDescription) { this.tourDescription.set(tourDescription); }

    public String getTourFrom() {
        return tourFrom.get();
    }

    public SimpleStringProperty tourFromProperty() {
        return tourFrom;
    }

    public void setTourFrom(String from) {
        this.tourFrom.set(from);
    }

    public String getTourTo() { return tourTo.get(); }

    public SimpleStringProperty tourToProperty() { return tourTo; }

    public void setTourTo(String to) { this.tourTo.set(to); }

    public String getTransportType() {
        return transportType.get();
    }

    public SimpleStringProperty transportTypeProperty() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType.set(transportType);
    }

    public TourService getTourService() {
        return tourService;
    }

    public void setTourService(TourService tourService) {
        this.tourService = tourService;
    }

    public TourListViewModel getTourListViewModel() {
        return tourListViewModel;
    }

    public void setTourListViewModel(TourListViewModel tourListViewModel) { this.tourListViewModel = tourListViewModel; }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) { this.tour = tour; }

    public void addNewTour(){

        //adding the new tour to the database
        Tour tour = Tour.builder().id(getId()).name(getName()).tourDescription(getTourDescription()).tourFrom(getTourFrom()).tourTo(getTourTo()).transportType(getTransportType()).build();
        try {
            tour = tourService.save(tour);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tourListViewModel.addItem(tour);
        logger.info("Added new tour {}", tour);
    }

}
