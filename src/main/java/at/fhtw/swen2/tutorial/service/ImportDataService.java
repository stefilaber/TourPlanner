package at.fhtw.swen2.tutorial.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportDataService {
    void importLogs(String path) throws Exception;

}
