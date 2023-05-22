package at.fhtw.swen2.tutorial.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface MapQuestApiService {

    String getTourDistanceTime(String from, String to, String routeType) throws IOException;
    String getStaticMap(JsonNode lastRequest) throws IOException;
}
