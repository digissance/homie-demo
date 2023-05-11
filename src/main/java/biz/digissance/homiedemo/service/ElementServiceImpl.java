package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.cloudinary.PhotoUpload;
import biz.digissance.homiedemo.domain.PhotoEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.PhotoRepository;
import com.cloudinary.Cloudinary;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ElementServiceImpl implements ElementService {

    private final ElementEntityRepository elementEntityRepository;
    private final PhotoRepository photoRepository;
    private final Cloudinary cloudinary;

    public ElementServiceImpl(final ElementEntityRepository elementEntityRepository,
                              final PhotoRepository photoRepository,
                              final Cloudinary cloudinary) {
        this.elementEntityRepository = elementEntityRepository;
        this.photoRepository = photoRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    @SneakyThrows
    public void addPhotoToElement(final Long id, final PhotoUpload photoUpload, final Authentication auth) {
//        PhotoUploadValidator validator = new PhotoUploadValidator();
//        validator.validate(photoUpload, result);
        final var elementEntity = elementEntityRepository.findById(id).orElseThrow();
        Map uploadResult = null;
        if (photoUpload.getFile() != null && !photoUpload.getFile().isEmpty()) {
            uploadResult = cloudinary.uploader().upload(photoUpload.getFile().getBytes(),
                    Map.of("folder", "homie/" + auth.getName() + "/" + elementEntity.getSpace().getId()+"/",
                            "resource_type", "auto"));
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
        }

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setTitle(elementEntity.getName());
        photoEntity.setUpload(photoUpload);
        photoEntity.setSecureURL(photoUpload.getSecureURL());
        photoEntity.setElement(elementEntity);

        elementEntity.setPhoto(photoEntity);
        elementEntityRepository.save(elementEntity);
        photoRepository.save(photoEntity);
    }
}
