package biz.digissance.homiedemo.cloudinary;

import biz.digissance.homiedemo.domain.PhotoEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.PhotoRepository;
import biz.digissance.homiedemo.service.photo.PhotoService;
import com.cloudinary.Cloudinary;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CloudinaryPhotoService implements PhotoService {

    private final ElementEntityRepository elementEntityRepository;
    private final PhotoRepository photoRepository;
    private final Cloudinary cloudinary;
    private final String cloudinaryFolder;

    public CloudinaryPhotoService(final ElementEntityRepository elementEntityRepository,
                                  final PhotoRepository photoRepository,
                                  final Cloudinary cloudinary,
                                  final @Value("${app.cloudinary.folder-name:homie-prod}") String cloudinaryFolder) {
        this.elementEntityRepository = elementEntityRepository;
        this.photoRepository = photoRepository;
        this.cloudinary = cloudinary;
        this.cloudinaryFolder = cloudinaryFolder;
    }

    @Override
    @SneakyThrows
    public void addPhotoToElement(final Long id, final PhotoUpload photoUpload, final Authentication auth) {
        final var elementEntity = elementEntityRepository.findById(id).orElseThrow();
        Map uploadResult = null;
        if (photoUpload.getFile() != null && !photoUpload.getFile().isEmpty()) {
            final var authorId = auth.getName();
            final var spaceId = elementEntity.getSpace().getId();
            final var destinationFolder = cloudinaryFolder + "/" + authorId + "/" + spaceId + "/";
            uploadResult = cloudinary.uploader().upload(photoUpload.getFile().getBytes(),
                    Map.of("folder", destinationFolder, "resource_type", "auto"));
            photoUpload.setPublicId((String) uploadResult.get("public_id"));
            Object version = uploadResult.get("version");
            if (version instanceof Integer) {
                photoUpload.setVersion(new Long((Integer) version));
            } else {
                photoUpload.setVersion((Long) version);
            }

            photoUpload.setSignature((String) uploadResult.get("signature"));
            photoUpload.setFormat((String) uploadResult.get("format"));
            photoUpload.setResourceType((String) uploadResult.get("resource_type"));
            photoUpload.setSecureURL((String) uploadResult.get("secure_url"));

            Optional.ofNullable(elementEntity.getPhoto())
                    .ifPresent(photo -> {
                        photo.setElement(null);
                        photoRepository.saveAndFlush(photo);
                    });

            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setTitle(elementEntity.getName());
            photoEntity.setUpload(photoUpload);
            photoEntity.setSecureURL(photoUpload.getSecureURL());

            photoEntity.setElement(elementEntity);
            elementEntity.setPhoto(photoEntity);

            photoRepository.save(photoEntity);
            elementEntityRepository.save(elementEntity);
        } else {
            throw new IllegalArgumentException("Cannot upload photo: " + photoUpload);
        }
    }

    @Override
    public void removeOrphanPhotos() {
        photoRepository.findByElementIsNull().forEach(this::removePhoto);
    }

    @SneakyThrows
    private void removePhoto(final PhotoEntity photoEntity) {
        final var publicId = photoEntity.getStoredFile().getPublicId();
        if (publicId != null) {
            Map result = cloudinary.uploader().destroy(publicId, Collections.emptyMap());
            if (result.get("result").equals("ok")) {
                log.info("photo deleted from cloudinary {}", photoEntity);
            } else {
                log.error("Image not deleted from cloudinary {}", photoEntity);
            }
        }
        photoRepository.delete(photoEntity);
        log.info("photo deleted from database {}", photoEntity);
    }
}
