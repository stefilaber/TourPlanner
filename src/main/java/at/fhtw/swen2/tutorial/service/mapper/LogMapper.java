package at.fhtw.swen2.tutorial.service.mapper;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import org.springframework.stereotype.Component;

@Component
public class LogMapper extends AbstractMapper<LogEntity, Log> {

    @Override
    public Log fromEntity(LogEntity entity) {
        return Log.builder()
                .id(entity.getId())
                .dateTime(entity.getDateTime())
                .comment(entity.getComment())
                .difficulty(entity.getDifficulty())
                .totalTime(entity.getTotalTime())
                .rating(entity.getRating())
                .tourId(entity.getTourId())
                .build();
    }

    @Override
    public LogEntity toEntity(Log log) {
        return LogEntity.builder()
                .id(log.getId())
                .dateTime(log.getDateTime())
                .comment(log.getComment())
                .difficulty(log.getDifficulty())
                .totalTime(log.getTotalTime())
                .rating(log.getRating())
                .tourId(log.getTourId())
                .build();
    }

}
