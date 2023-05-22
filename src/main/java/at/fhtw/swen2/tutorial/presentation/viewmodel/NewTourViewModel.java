package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.beans.property.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NewTourViewModel {
    private SimpleLongProperty id = new SimpleLongProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty tourDescription  = new SimpleStringProperty();
    private SimpleStringProperty tourFrom  = new SimpleStringProperty();
    private SimpleStringProperty tourTo  = new SimpleStringProperty();
    private SimpleStringProperty transportType  = new SimpleStringProperty();
    private SimpleIntegerProperty tourDistance  = new SimpleIntegerProperty();
    private SimpleIntegerProperty estimatedTime  = new SimpleIntegerProperty();

    @Autowired
    private TourService tourService;
    @Autowired
    private TourListViewModel tourListViewModel;

    private Tour tour;

    public NewTourViewModel() { }

    public NewTourViewModel(Tour tour) {
        this.tour = tour;
        this.id = new SimpleLongProperty(tour.getId());
        this.name = new SimpleStringProperty(tour.getName());
        this.tourDescription = new SimpleStringProperty(tour.getTourDescription());
        this.tourFrom = new SimpleStringProperty(tour.getTourFrom());
        this.tourTo = new SimpleStringProperty(tour.getTourTo());
        this.transportType = new SimpleStringProperty(tour.getTransportType());
        this.tourDistance = new SimpleIntegerProperty(tour.getTourDistance());
        this.estimatedTime = new SimpleIntegerProperty(tour.getEstimatedTime());
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

    public int getTourDistance() {
        return tourDistance.get();
    }

    public SimpleIntegerProperty tourDistanceProperty() {
        return tourDistance;
    }

    public void setTourDistance(int tourDistance) {
        this.tourDistance.set(tourDistance);
    }

    public int getEstimatedTime() {
        return estimatedTime.get();
    }

    public SimpleIntegerProperty estimatedTimeProperty() { return estimatedTime; }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime.set(estimatedTime);
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

    public void addNewTour() {
        Tour tour = Tour.builder().id(getId()).name(getName()).tourDescription(getTourDescription()).tourFrom(getTourFrom()).tourTo(getTourTo()).transportType(getTransportType()).tourDistance(getTourDistance()).estimatedTime(getEstimatedTime()).build();
        tour = tourService.addNew(tour);
        tourListViewModel.addItem(tour);
    }

}
