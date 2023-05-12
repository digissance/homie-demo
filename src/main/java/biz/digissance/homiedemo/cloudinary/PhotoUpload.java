package biz.digissance.homiedemo.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.StoredFile;
import com.cloudinary.Transformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class PhotoUpload extends StoredFile {

    private String title;
    private MultipartFile file;
    private String secureURL;

    public String getUrl(Cloudinary cloudinary) {
        if (version != null && format != null && publicId != null) {
            return cloudinary.url()
                    .resourceType(resourceType)
                    .type(type)
                    .format(format)
                    .version(version)
                    .generate(publicId);
        } else {
            return null;
        }
    }

    public String getThumbnailUrl() {
        if (version != null && format != null && publicId != null) {
            return Singleton.getCloudinary().url().format(format)
                    .resourceType(resourceType)
                    .type(type)
                    .version(version).transformation(new Transformation().width(150).height(150).crop("fit"))
                    .generate(publicId);
        } else {
            return null;
        }
    }

    public String getComputedSignature() {
        return getComputedSignature(Singleton.getCloudinary());
    }

    public boolean validSignature() {
        return getComputedSignature().equals(signature);
    }

    public void setSecureURL(final String secureURL) {
        this.secureURL = secureURL;
    }

    public String getSecureURL() {
        return secureURL;
    }
}