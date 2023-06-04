package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.LogRepository;
import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import at.fhtw.swen2.tutorial.service.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogMapper logMapper;


    @Override
    public List<Log> getLogList(Long selectedTourId) {
        return logMapper.fromEntity(logRepository.findByTourId(selectedTourId));
    }

    @Override
    public Log addNew(Log log) {
        if (log == null){
            return null;
        }
        LogEntity entity = logRepository.save(logMapper.toEntity(log));
        return logMapper.fromEntity(entity);
    }

    @Override
    public void delete(Log log) {
        logRepository.delete(logMapper.toEntity(log));
    }

    @Override
    public void edit(Log log) {
        LogEntity entity = logRepository.findById(log.getId()).orElse(null);
        System.out.println(log);
        String dateTime = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime());
        entity.setDateTime(dateTime);
        entity.setComment(log.getComment());
        entity.setDifficulty(log.getDifficulty());
        entity.setTotalTime(log.getTotalTime());
        entity.setRating(log.getRating());
        logRepository.save(entity);
    }
}
