package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Image;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.ImageRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import Plant.PlantProject.common.util.FileUtils;
import Plant.PlantProject.vo.response.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ImageFileUploadService {

    private final AwsS3Service awsS3Service;
    private final TradeBoardRepository tradeBoardRepository;
    private final ImageRepository imageRepository;


    public List<Image> saveImages(List<MultipartFile> files, TradeBoard tradeBoard) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() > 1048576) {
                throw ErrorCode.throwMaxFileSizeExceeded();
            }
            Image image = uploadImage(file, tradeBoard);
            images.add(image);

            tradeBoard.addImageList(image);
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