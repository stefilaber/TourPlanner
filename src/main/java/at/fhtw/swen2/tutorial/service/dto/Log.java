package at.fhtw.swen2.tutorial.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Log {
    private Long id;
    private Long tourId;
    private String dateTime;
    private String comment;
    private String difficulty;
    private int totalTime;
    private int rating;
}
