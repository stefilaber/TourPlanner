package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Log> getLogList(Long selectedTourId) {

        log.info("Log list retrieved from db");
        return logMapper.fromEntity(logRepository.findByTourId(selectedTourId));
    }

    @Override
    public Log save(Log log) {
        if (log == null){
            return null;
        }
        LogEntity entity = logRepository.save(logMapper.toEntity(log));
        LogServiceImpl.log.info("Log saved to db");
        return logMapper.fromEntity(entity);
    }

    @Override
    public void delete(Log log) {
        LogServiceImpl.log.info("Log deleted from db");
        logRepository.delete(logMapper.toEntity(log));
    }

}
