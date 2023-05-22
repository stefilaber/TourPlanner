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

}
