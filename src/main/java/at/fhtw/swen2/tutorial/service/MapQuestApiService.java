package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.TourDistanceTime;

public interface MapQuestApiService {

    TourDistanceTime getTourDistanceTime(String from, String to);
}
