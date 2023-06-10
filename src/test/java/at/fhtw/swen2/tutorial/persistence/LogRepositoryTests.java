package at.fhtw.swen2.tutorial.persistence;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LogRepositoryTests {
    @Mock
    private LogRepository logRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private LogEntity createLogEntity() {
        return LogEntity.builder()
                .id(1L)
                .tourId(1L)
                .dateTime("2021/05/05 - 12:00")
                .comment("nice")
                .difficulty("easy")
                .totalTime("5:00")
                .rating(1)
                .build();
    }

    @Test
    void shouldSaveLogEntity() {
        LogEntity logEntity = createLogEntity();

        logRepository.save(logEntity);

        // Verify that the save method is called once with the specified tourEntity
        verify(logRepository, times(1)).save(logEntity);
    }

    @Test
    void shouldReturnListOfLogEntities() {
        LogEntity logEntity = createLogEntity();

        List<LogEntity> logEntities = Arrays.asList(logEntity);

        when(logRepository.findAll()).thenReturn(logEntities);

        logRepository.findAll().forEach(System.out::println);

        // Verify that the save method is never called
        verify(logRepository, never()).save(any());

        // Verify that the findAll method is called once
        verify(logRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoLogEntitiesExist() {
        when(logRepository.findAll()).thenReturn(Arrays.asList());

        List<LogEntity> result = logRepository.findAll();

        assertTrue(result.isEmpty());

        verify(logRepository, never()).save(any());
        verify(logRepository, times(1)).findAll();
    }

    @Test
    void shouldFindLogEntityById() {
        LogEntity logEntity = createLogEntity();

        when(logRepository.findById(1L)).thenReturn(Optional.of(logEntity));

        Optional<LogEntity> result = logRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(logEntity, result.get());

        verify(logRepository, never()).save(any());
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    void findByTourIdShouldReturnListOfLogEntities() {
        Long tourId = 1L;
        List<LogEntity> expectedEntities = Collections.singletonList(new LogEntity());

        when(logRepository.findByTourId(tourId)).thenReturn(expectedEntities);

        List<LogEntity> result = logRepository.findByTourId(tourId);

        assertEquals(expectedEntities, result);
        verify(logRepository, times(1)).findByTourId(tourId);
    }

    @Test
    void deleteByTourIdShouldDeleteLogsByTourId() {
        Long tourId = 1L;

        logRepository.deleteByTourId(tourId);

        verify(logRepository, times(1)).deleteByTourId(tourId);
    }
}
