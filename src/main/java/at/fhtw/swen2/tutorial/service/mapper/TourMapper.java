package at.fhtw.swen2.tutorial.service.mapper;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<TourEntity, Tour> {

    @Override
    public Tour fromEntity(TourEntity entity) {
        return Tour.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tourDescription(entity.getTourDescription())
                .tourFrom(entity.getTourFrom())
                .tourTo(entity.getTourTo())
                .transportType(entity.getTransportType())
                .tourDistance(entity.getTourDistance())
                .estimatedTime(entity.getEstimatedTime())
                .build();
    }

    @Override
    public TourEntity toEntity(Tour tour) {
        return TourEntity.builder()
                .id(tour.getId())
                .name(tour.getName())
                .tourDescription(tour.getTourDescription())
                .tourFrom(tour.getTourFrom())
                .tourTo(tour.getTourTo())
                .transportType(tour.getTransportType())
                .tourDistance(tour.getTourDistance())
                .estimatedTime(tour.getEstimatedTime())
                .build();
    }

}
