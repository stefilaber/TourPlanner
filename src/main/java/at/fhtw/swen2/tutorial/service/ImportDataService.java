package at.fhtw.swen2.tutorial.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImportDataService {

    void importData(String path) throws IOException;
}
