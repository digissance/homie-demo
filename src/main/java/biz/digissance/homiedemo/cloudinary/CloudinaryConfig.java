package biz.digissance.homiedemo.cloudinary;

import com.cloudinary.Cloudinary;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = Map.of();
        return new Cloudinary(config);
    }
}
