package biz.digissance.homiedemo.cloudinary;

import biz.digissance.homiedemo.service.ElementService;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elements")
public class PhotoController {

    private final ElementService service;

    public PhotoController(final ElementService service) {
        this.service = service;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<Void> uploadPhoto(final @PathVariable Long id,
                                            final @ModelAttribute PhotoUpload photoUpload,
                                            final Authentication auth,
                                            final BindingResult result)
            throws IOException {
        service.addPhotoToElement(id,photoUpload,auth);
        return ResponseEntity.noContent().build();
//        }
    }
}