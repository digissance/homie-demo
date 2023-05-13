package biz.digissance.homiedemo.service.photo;

import biz.digissance.homiedemo.cloudinary.PhotoUpload;
import biz.digissance.homiedemo.domain.PhotoEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.PhotoRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@Profile({"!cloudinary"})
public class LocalDevPhotoService implements PhotoService {

    private final ElementEntityRepository elementEntityRepository;
    private final PhotoRepository photoRepository;

    public LocalDevPhotoService(
            final ElementEntityRepository elementEntityRepository,
            final PhotoRepository photoRepository) {
        this.elementEntityRepository = elementEntityRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public void addPhotoToElement(final Long id, final PhotoUpload photoUpload, final Authentication auth) {

        final var elementEntity = elementEntityRepository.findById(id).orElseThrow();
        Optional.ofNullable(elementEntity.getPhoto())
                .ifPresent(photo -> {
                    photo.setElement(null);
                    photoRepository.saveAndFlush(photo);
                });
        final var photoEntity = new PhotoEntity();
        photoEntity.setTitle(elementEntity.getName());
        photoEntity.setImage(elementEntity.getName().concat(".jpg"));
        photoEntity.setSecureURL(elementEntity.getName().concat(".jpg"));
        photoEntity.setElement(elementEntity);
        elementEntity.setPhoto(photoEntity);
        photoRepository.save(photoEntity);
        elementEntityRepository.save(elementEntity);
    }

    @Override
    public void removeOrphanPhotos() {
        final var photosDeleted = photoRepository.deleteAllByElementIsNull();
        log.info("{} photos deleted", photosDeleted);
    }
}
