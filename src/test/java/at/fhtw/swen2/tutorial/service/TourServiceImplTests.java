package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import at.fhtw.swen2.tutorial.service.mapper.TourMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TourServiceImplTests {
    @Mock
    private TourRepository tourRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private TourMapper tourMapper;

    @InjectMocks
    private TourServiceImpl tourServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDeleteTour() {
        Tour tour = new Tour();
        tour.setId(1L);

        tourServiceImpl.delete(tour);

        verify(logRepository, times(1)).deleteByTourId(tour.getId());
        verify(tourRepository, times(1)).delete(any());
    }

    @Test
    void saveShouldReturnNullWhenTourIsNull() throws IOException {
        Tour result = tourServiceImpl.save(null);
        assertNull(result);
        verifyNoMoreInteractions(tourRepository, tourMapper);
    }

}
