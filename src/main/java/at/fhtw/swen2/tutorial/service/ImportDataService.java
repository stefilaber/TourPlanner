package at.fhtw.swen2.tutorial.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ImportDataService<T> {

    List<T> importData(File file) throws IOException;
}
