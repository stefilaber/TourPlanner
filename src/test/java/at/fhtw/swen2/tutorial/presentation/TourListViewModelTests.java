package at.fhtw.swen2.tutorial.presentation;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TourListViewModelTests {
    private TourListViewModel tourListViewModel;

    @Mock
    private TourService tourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourListViewModel = new TourListViewModel(tourService, null, null,null);
    }

    private Tour createTour() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setName("Test Tour");
        tour.setTourDescription("Test description");
        tour.setTourFrom("Start Location");
        tour.setTourTo("End Location");
        tour.setTransportType("Car");
        return tour;
    }

    @Test
    void addItemShouldAddTourToList() {
        Tour tour = createTour();

        ObservableList<Tour> tourListItems = tourListViewModel.getTourListItems();

        tourListViewModel.addItem(tour);

        assertTrue(tourListItems.contains(tour));
        assertTrue(tourListViewModel.getTourListItems().contains(tour));
    }

    @Test
    void initListShouldLoadToursFromTourService() {
        Tour tour1 = createTour();
        Tour tour2 = createTour();

        List<Tour> tourList = new ArrayList<>();
        tourList.add(tour1);
        tourList.add(tour2);

        when(tourService.getTourList()).thenReturn(tourList);

        ObservableList<Tour> tourListItems = tourListViewModel.getTourListItems();

        tourListViewModel.initList();

        assertTrue(tourListItems.contains(tour1));
        assertTrue(tourListItems.contains(tour2));
        assertTrue(tourListViewModel.getTourListItems().contains(tour1));
        assertTrue(tourListViewModel.getTourListItems().contains(tour2));

        verify(tourService, times(1)).getTourList();
    }

    @Test
    void deleteTourShouldDeleteTourFromList() {
        Tour tour = createTour();

        ObservableList<Tour> tourListItems = tourListViewModel.getTourListItems();
        tourListItems.add(tour);

        tourListViewModel.deleteTour(tour);

        assertFalse(tourListItems.contains(tour));
        assertFalse(tourListViewModel.getTourListItems().contains(tour));
        verify(tourService, times(1)).delete(tour);
    }

    @Test
    void saveEditedTourShouldSaveTourAndUpdateList() throws IOException {
        Tour tour = createTour();

        ObservableList<Tour> tourListItems = tourListViewModel.getTourListItems();
        tourListItems.add(tour);

        tourListViewModel.saveEditedTour(tour);

        verify(tourService, times(1)).save(tour);
        assertTrue(tourListItems.containsAll(tourListViewModel.getTourListItems()));
    }

    @Test
    void deleteMapShouldCallTourServiceDeleteMap() {
        String mapName = "Test Map";

        tourListViewModel.deleteMap(mapName);

        verify(tourService, times(1)).deleteMap(mapName);
    }

}
