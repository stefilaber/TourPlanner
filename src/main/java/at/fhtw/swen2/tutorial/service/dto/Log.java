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
    private String totalTime;
    private int rating;

    public Log() {
        // Default constructor
    }

    public Log(Long id, Long tourId, String dateTime, String comment, String difficulty, String totalTime, int rating) {
        this.id = id;
        this.tourId = tourId;
        this.dateTime = dateTime;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.rating = rating;
    }
}
