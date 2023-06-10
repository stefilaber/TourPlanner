package at.fhtw.swen2.tutorial.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tour {

    private Long id;
    private String name;
    private String tourDescription;
    private String tourFrom;
    private String tourTo;
    private String transportType;
    private int tourDistance;
    private int estimatedTime;

    public Tour() {
        // Default constructor
    }

    public Tour(Long id, String name, String tourDescription, String tourFrom, String tourTo, String transportType, int tourDistance, int estimatedTime) {
        this.id = id;
        this.name = name;
        this.tourDescription = tourDescription;
        this.tourFrom = tourFrom;
        this.tourTo = tourTo;
        this.transportType = transportType;
        this.tourDistance = tourDistance;
        this.estimatedTime = estimatedTime;
    }

}
