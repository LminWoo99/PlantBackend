package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Image;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.repository.ImageRepository;
import Plant.PlantProject.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageFileUploadService {

    private final AwsS3Service awsS3Service;
    private final TradeBoardService tradeBoardService;
    private final ImageRepository imageRepository;

    @Transactional
    public List<Image> upload(Long tradeBoardId, List<MultipartFile> files) throws IOException {
        log.info("1차 upload 통과");
        TradeBoard tradeBoard = tradeBoardService.findTradeBoardById(tradeBoardId);
        List<Image> upload = upload(files, tradeBoard);
        return upload;
    }

    private List<Image> upload(List<MultipartFile> files, TradeBoard tradeBoard) throws IOException {
        log.info("2차 upload 통과");
        List<Image> images = uploadImageToStorageServer(files, tradeBoard);
        imageRepository.saveAll(images);
        return images;
    }

    private List<Image> uploadImageToStorageServer(List<MultipartFile> files, TradeBoard tradeBoard) throws IOException {
        log.info("4차 upload 통과");
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = FileUtils.getRandomFilename();
            String filepath = awsS3Service.upload(file, filename);
            images.add(Image.builder()
                    .name(filename)
                    .url(filepath)
                    .tradeBoard(tradeBoard)
                    .build());
        }

        return images;
    }
    public List<Image> findImageByTradeBoardId(Long id){
        List<Image> images = imageRepository.findImageByTradeBoardId(id);
        return images;
    }

}