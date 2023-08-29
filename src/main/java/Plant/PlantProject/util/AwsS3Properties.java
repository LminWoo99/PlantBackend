package Plant.PlantProject.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Properties {
    @Value("${aws.s3.credentials.accessKey}")
    private String accessKey;
    @Value("${aws.s3.credentials.secretKey}")
    private String secretKey;
    @Value("${aws.s3.s3.bucket}")
    private String bucket;
    @Value("${aws.s3.region.static}")
    private String region;

    private String uploadPath;
    @Value("${aws.s3.end-point}")
    private String endPoint;
}
