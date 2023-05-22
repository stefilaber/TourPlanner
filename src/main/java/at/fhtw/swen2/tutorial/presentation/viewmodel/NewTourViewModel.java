package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.MapQuestApiService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class NewTourViewModel {
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

    @Autowired
    private MapQuestApiService mapQuestApiService;

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

    public void addNewTour() throws IOException {

        //first request for the duration and time:
        String responseJsonString = mapQuestApiService.getTourDistanceTime(getTourFrom(), getTourTo(), getTransportType());

        //mapping the response to a json object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(responseJsonString);

        //getting the distance and time from the response
        JsonNode route = response.get("route");
        JsonNode distance = route.get("distance");
        JsonNode time = route.get("time");

        //second request for the static map:
        responseJsonString = mapQuestApiService.getStaticMap(response);
        byte[] byteArrray = responseJsonString.getBytes();
        System.out.println(byteArrray);
        System.out.println(byteArrray.length);

        //adding the new tour to the database
//        Tour tour = Tour.builder().id(getId()).name(getName()).tourDescription(getTourDescription()).tourFrom(getTourFrom()).tourTo(getTourTo()).transportType(getTransportType()).tourDistance(distance.asInt()).estimatedTime(time.asInt()).build();
//        tour = tourService.addNew(tour);
//        tourListViewModel.addItem(tour);

    }

}
