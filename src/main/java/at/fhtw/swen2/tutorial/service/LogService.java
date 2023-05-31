package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.Log;

import java.util.List;

public interface LogService {

    List<Log> getLogList(Long tourId);

    Log addNew(Log log);

    void deleteLog(Log log);

    // erweitern mit parameter create new service

}
