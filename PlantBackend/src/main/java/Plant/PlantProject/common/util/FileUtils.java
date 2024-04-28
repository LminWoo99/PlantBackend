package Plant.PlantProject.common.util;

import Plant.PlantProject.common.exception.ErrorCode;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {

    private static final String BASE_DIRECTORY = "image";

    public static String getRandomFilename() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getFilePath(MultipartFile file, String filename) {
        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if(!isValidFileType(extension)) {
            throw ErrorCode.throwUnSupportedFile();
        }
        return  BASE_DIRECTORY + "/" + filename + "." + extension;
    }

    private static boolean isValidFileType(String extension) {
        return Arrays.stream(FileType.values())
                .anyMatch(type -> type.getExtension().equals(extension));
    }

}