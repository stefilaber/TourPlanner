package at.fhtw.swen2.tutorial.utils;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.service.dto.Person;
import org.junit.jupiter.api.Test;

public class BuilderTest {

    @Test
    void testPersonEntityBuilder() {
        TourEntity maxi = TourEntity.builder()
                .name("Maxi")
                .email("maxi@email.com")
                .build();
    }
    @Test
    void testPersonBuilder() {
        Person maxi = Person.builder()
                .name("Maxi")
                .id(11L)
                .build();
    }


}
