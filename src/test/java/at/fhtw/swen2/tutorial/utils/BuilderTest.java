package at.fhtw.swen2.tutorial.utils;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuilderTest {

    @Test
    void testTourEntityBuilder() {

        TourEntity tourEntity = TourEntity.builder()
                .name("Vorarlberg")
                .tourDescription("Vorarlberg Description")
                .tourFrom("Dornbirn")
                .tourTo("Bregenz")
                .transportType("car")
                .tourDistance(45)
                .estimatedTime(5)
                .build();

        assertEquals("Vorarlberg", tourEntity.getName());
        assertEquals("Vorarlberg Description", tourEntity.getTourDescription());
        assertEquals("Dornbirn", tourEntity.getTourFrom());
        assertEquals("Bregenz", tourEntity.getTourTo());
        assertEquals("car", tourEntity.getTransportType());
        assertEquals(45, tourEntity.getTourDistance());
        assertEquals(5, tourEntity.getEstimatedTime());
    }

    @Test
    void testLogEntityBuilder() {
        // Create a TourEntity for association
        TourEntity tourEntity = new TourEntity();
        tourEntity.setId(1L);
        tourEntity.setName("Vorarlberg");
        tourEntity.setTourDescription("Vorarlberg");
        tourEntity.setTourFrom("Dornbirn");
        tourEntity.setTourTo("Bregenz");
        tourEntity.setTransportType("car");
        tourEntity.setTourDistance(45);
        tourEntity.setEstimatedTime(5);

        // Create a LogEntity
        LogEntity logEntity = LogEntity.builder()
                .id(1L)
                .dateTime("2023/06/09 10:30")
                .comment("Test comment")
                .difficulty("Medium")
                .totalTime("2 hours")
                .rating(4)
                .tourId(1L)
                .tour(tourEntity)
                .build();

        // Verify the LogEntity
        assertEquals(1L, logEntity.getId());
        assertEquals("2023/06/09 10:30", logEntity.getDateTime());
        assertEquals("Test comment", logEntity.getComment());
        assertEquals("Medium", logEntity.getDifficulty());
        assertEquals("2 hours", logEntity.getTotalTime());
        assertEquals(4, logEntity.getRating());
        assertEquals(1L, logEntity.getTourId());
        assertNotNull(logEntity.getTour());
        assertEquals(tourEntity, logEntity.getTour());
    }

}
