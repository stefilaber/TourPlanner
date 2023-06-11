package at.fhtw.swen2.tutorial.service;
import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.impl.LogServiceImpl;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogServiceImplTests {

    @Mock
    private LogRepository logRepository;

    @Mock
    private LogMapper logMapper;

    @InjectMocks
    private LogServiceImpl logServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLogListShouldReturnListOfLogs() {
        Long selectedTourId = 1L;
        LogEntity logEntity = new LogEntity();
        Log log = new Log();
        List<LogEntity> logEntities = Collections.singletonList(logEntity);
        List<Log> expectedLogs = Collections.singletonList(log);

        when(logRepository.findByTourId(selectedTourId)).thenReturn(logEntities);
        when(logMapper.fromEntity(logEntities)).thenReturn(expectedLogs);

        List<Log> result = logServiceImpl.getLogList(selectedTourId);

        assertEquals(expectedLogs, result);
        verify(logRepository, times(1)).findByTourId(selectedTourId);
        verify(logMapper, times(1)).fromEntity(logEntities);
    }

    @Test
    void getLogListShouldReturnEmptyListWhenNoLogsFound() {
        Long selectedTourId = 1L;
        List<LogEntity> logEntities = Collections.emptyList();
        List<Log> expectedLogs = Collections.emptyList();

        when(logRepository.findByTourId(selectedTourId)).thenReturn(logEntities);
        when(logMapper.fromEntity(logEntities)).thenReturn(expectedLogs);

        List<Log> result = logServiceImpl.getLogList(selectedTourId);

        assertEquals(expectedLogs, result);
        verify(logRepository, times(1)).findByTourId(selectedTourId);
        verify(logMapper, times(1)).fromEntity(logEntities);
    }

    @Test
    void saveShouldReturnSavedLog() {
        Log log = new Log();
        LogEntity logEntity = new LogEntity();

        when(logMapper.toEntity(log)).thenReturn(logEntity);
        when(logRepository.save(logEntity)).thenReturn(logEntity);
        when(logMapper.fromEntity(logEntity)).thenReturn(log);

        Log result = logServiceImpl.save(log);

        assertEquals(log, result);
        verify(logMapper, times(1)).toEntity(log);
        verify(logRepository, times(1)).save(logEntity);
        verify(logMapper, times(1)).fromEntity(logEntity);
    }

    @Test
    void saveShouldReturnNullWhenLogIsNull() {
        Log result = logServiceImpl.save(null);

        assertNull(result);
        verifyNoMoreInteractions(logRepository, logMapper);
    }

    @Test
    void deleteShouldDeleteLog() {
        Log log = new Log();
        LogEntity logEntity = new LogEntity();

        when(logMapper.toEntity(log)).thenReturn(logEntity);

        logServiceImpl.delete(log);

        verify(logMapper, times(1)).toEntity(log);
        verify(logRepository, times(1)).delete(logEntity);
    }
}

