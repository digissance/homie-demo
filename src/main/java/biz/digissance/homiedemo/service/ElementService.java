package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.cloudinary.PhotoUpload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface ElementService {

    @PreAuthorize("hasPermission(#id,'EDIT')")
    void addPhotoToElement(Long id, PhotoUpload photoUpload, Authentication auth);
}
