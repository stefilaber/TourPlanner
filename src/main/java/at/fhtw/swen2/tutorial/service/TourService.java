package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.Tour;

import java.io.IOException;
import java.util.List;

public interface TourService {

    List<Tour> getTourList();

    Tour save(Tour tour) throws IOException;

    void delete(Tour tour);

    void deleteMap(String name);

}
