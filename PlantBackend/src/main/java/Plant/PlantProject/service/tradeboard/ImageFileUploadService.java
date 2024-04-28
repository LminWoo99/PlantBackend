package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Image;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.ImageRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import Plant.PlantProject.common.util.FileUtils;
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
    private final TradeBoardRepository tradeBoardRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public List<Image> uploadImagesToTradeBoard(Long tradeBoardId, List<MultipartFile> files) throws IOException {
        log.info("Uploading images for TradeBoard ID: {}", tradeBoardId);
        TradeBoard tradeBoard = tradeBoardRepository.findTradeBoardById(tradeBoardId);
        List<Image> uploadedImages = saveImages(files, tradeBoard);
        return uploadedImages;
    }

    private List<Image> saveImages(List<MultipartFile> files, TradeBoard tradeBoard) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() > 1048576) {
                throw ErrorCode.throwMaxFileSizeExceeded();
            }
            images.add(uploadImage(file, tradeBoard));
        }
        imageRepository.saveAll(images);
        return images;
    }

    private Image uploadImage(MultipartFile file, TradeBoard tradeBoard) throws IOException {
        log.info("Uploading image to storage server for file: {}", file.getOriginalFilename());
        String filename = FileUtils.getRandomFilename();
        String filepath = awsS3Service.upload(file, filename);
        return Image.builder()
                .name(filename)
                .url(filepath)
                .tradeBoard(tradeBoard)
                .build();
    }
    @Transactional(readOnly = true)
    public List<Image> findImagesByTradeBoardId(Long id) {
        return imageRepository.findImageByTradeBoardId(id);
    }

}