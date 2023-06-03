package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import at.fhtw.swen2.tutorial.service.MapQuestApiService;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.mapper.TourMapper;
import at.fhtw.swen2.tutorial.service.TourService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private TourMapper tourMapper;


    @Autowired
    private MapQuestApiService mapQuestApiService;


    @Override
    public List<Tour> getTourList() {
        System.out.println(tourMapper.fromEntity(tourRepository.findAll()));
        return tourMapper.fromEntity(tourRepository.findAll());
    }

    @Override
    public Tour save(Tour tour) throws IOException {
        if (tour == null){
            return null;
        }

        //first request for the duration and time:
        String responseJsonString = mapQuestApiService.getTourDistanceTime(tour.getTourFrom(), tour.getTourTo(), tour.getTransportType());

        //mapping the response to a json object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(responseJsonString);

        //getting the distance and time from the response
        JsonNode route = response.get("route");
        JsonNode distance = route.get("distance");
        JsonNode time = route.get("time");

        //second request for the static map:
        BufferedImage map = mapQuestApiService.getStaticMap(response);

        //creating a maps directory in case it doesn't exist
        String path = new java.io.File(".").getCanonicalPath() + "\\src\\main\\resources\\maps\\";
        Files.createDirectories(Paths.get(path));

        //saving the image in the maps folder
        String mapPath = tour.getName() + ".png";
        try(FileOutputStream fos = new FileOutputStream(path + mapPath)) {
            ImageIO.write(map, "png", fos);
        } catch (IOException e) {
            // TODO Add logging
            throw e;
        }

        //rebuilding the tour with distance, time and map path
        tour = Tour.builder().id(tour.getId()).name(tour.getName()).tourDescription(tour.getTourDescription()).tourFrom(tour.getTourFrom()).tourTo(tour.getTourTo()).transportType(tour.getTransportType()).tourDistance(distance.asInt()).estimatedTime(time.asInt()).build();
        //saving the tour in the database
        TourEntity entity = tourRepository.save(tourMapper.toEntity(tour));
        return tourMapper.fromEntity(entity);
    }

    @Override
    public void delete(Tour tour) {
        logRepository.deleteByTourId(tour.getId());
        tourRepository.delete(tourMapper.toEntity(tour));
    }

    @Override
    public void deleteMap(String name) {
        try {
            String path = new java.io.File(".").getCanonicalPath() + "\\src\\main\\resources\\maps\\" + name + ".png";
            System.out.println(path);
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
