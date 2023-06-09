package at.fhtw.swen2.tutorial.service.impl;
import at.fhtw.swen2.tutorial.service.MapQuestApiService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Service
@Slf4j
public class MapQuestApiServiceImpl implements MapQuestApiService {
    String key = "kMmPCHrWU9Jng3AWsYyivKIACMsJqaRO";

    @Override
    public String getTourDistanceTime(String from, String to, String routeType) throws IOException {

        URL url = new URL("http://www.mapquestapi.com/directions/v2/route?key=" + key + "&from=" + from + "&to=" + to + "&routeType=" + routeType + "&unit=k");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        log.debug("Requesting distance and time from MapQuest API");

        //reading the response
        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();
        log.info("Distance and time received from MapQuest API");
        return content.toString();

    }

    public BufferedImage getStaticMap(JsonNode lastRequest) throws IOException {

        JsonNode route = lastRequest.get("route");
        JsonNode session = route.get("sessionId");
        String boundingBox = route.get("boundingBox").get("lr").get("lng").asText() + "," + route.get("boundingBox").get("lr").get("lat").asText() + "," + route.get("boundingBox").get("ul").get("lng").asText() + "," + route.get("boundingBox").get("ul").get("lat").asText();
        URL url = new URL("https://www.mapquestapi.com/staticmap/v5/map?key=" + key + "&session=" + session.asText() + "&bondingBox=" + boundingBox+ "&size=600,200@2x");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        log.debug("Requesting static map from MapQuest API");

        //reading the response
        int status = con.getResponseCode();
        BufferedImage img = ImageIO.read(con.getInputStream());

        con.disconnect();
        log.info("Static map received from MapQuest API");
        return img;
    }

}
