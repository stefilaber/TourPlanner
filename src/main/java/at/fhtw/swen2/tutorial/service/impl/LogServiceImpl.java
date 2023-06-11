package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogMapper logMapper;

    private static Logger logger = LogManager.getLogger(LogServiceImpl.class);


    @Override
    public List<Log> getLogList(Long selectedTourId) {

        logger.info("Log list retrieved from db");
        return logMapper.fromEntity(logRepository.findByTourId(selectedTourId));
    }

    @Override
    public Log save(Log log) {
        if (log == null){
            return null;
        }
        LogEntity entity = logRepository.save(logMapper.toEntity(log));
        logger.info("Log saved to db");
        return logMapper.fromEntity(entity);
    }

    @Override
    public void delete(Log log) {
        logger.info("Log deleted from db");
        logRepository.delete(logMapper.toEntity(log));
    }

}
