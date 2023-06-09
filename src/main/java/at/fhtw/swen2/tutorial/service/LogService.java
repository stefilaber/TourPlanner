package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.service.dto.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface LogService {

    List<Log> getLogList(Long tourId);

    Log save(Log log);

    void delete(Log log);

//    void importLogsFromExcel(String path) throws IOException;
}
