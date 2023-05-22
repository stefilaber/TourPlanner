package at.fhtw.swen2.tutorial.service.impl;
import at.fhtw.swen2.tutorial.service.MapQuestApiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.stream.ImageInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.Files;

@Service
@Transactional
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

        return content.toString();

    }

    public String getStaticMap(JsonNode lastRequest) throws IOException {

        JsonNode route = lastRequest.get("route");
        JsonNode session = route.get("sessionId");
        String boundingBox = route.get("boundingBox").get("lr").get("lng").asText() + "," + route.get("boundingBox").get("lr").get("lat").asText() + "," + route.get("boundingBox").get("ul").get("lng").asText() + "," + route.get("boundingBox").get("ul").get("lat").asText();
        URL url = new URL("https://www.mapquestapi.com/staticmap/v5/map?key=" + key + "&session=" + session.asText() + "&bondingBox=" + boundingBox + "&size=600,200@2x");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        //reading the response
        int status = con.getResponseCode();

//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer content = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();

        ImageInputStream img = (ImageInputStream) con.getInputStream();
        byte[] content = Files.readAllBytes(img);


        con.disconnect();

        byte[] imgByteArray = Base64.decodeBase64(String.valueOf(content));
        FileOutputStream imgOutFile = new FileOutputStream("C:\\Users\\stefi\\OneDrive\\Desktop\\test.jpeg");
        imgOutFile.write(imgByteArray);

        imgOutFile.close();
        return content.toString();
    }

}
