package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.Log;

import java.util.List;

public interface LogService {

    List<Log> getLogList();

    Log addNew(Log log);

    // erweitern mit parameter create new service

}
