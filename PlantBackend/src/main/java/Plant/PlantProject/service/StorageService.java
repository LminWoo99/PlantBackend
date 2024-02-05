package Plant.PlantProject.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    public String upload(MultipartFile file, String filename) throws IOException;
}