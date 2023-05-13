package biz.digissance.homiedemo.cloudinary;

import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.PhotoRepository;
import biz.digissance.homiedemo.service.photo.PhotoService;
import com.cloudinary.Cloudinary;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(final @Value("${CLOUDINARY_NAME:name}") String cloudName,
                                 final @Value("${CLOUDINARY_API_KEY:key}") String apiKey,
                                 final @Value("${CLOUDINARY_API_SECRET:secret}") String apiSecret) {
        Map<String, String> config = Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret);
        return new Cloudinary(config);
    }

    @Bean
    public PhotoService cloudinaryPhotoService(
            final ElementEntityRepository elementRepository,
            final PhotoRepository photoRepository, final Cloudinary cloudinary) {
        return new CloudinaryPhotoService(elementRepository, photoRepository, cloudinary);
    }
}
