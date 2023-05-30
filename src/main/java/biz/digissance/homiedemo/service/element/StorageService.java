package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import org.springframework.security.access.prepost.PreAuthorize;

public interface StorageService {

    @PreAuthorize("hasPermission(#storageId,'EDIT')")
    ItemDto createItem(long storageId, CreateElementRequest request);

    @PreAuthorize("hasPermission(#parentId,'EDIT')")
    StorageDto createStorage(long parentId, CreateElementRequest request);

    @PreAuthorize("hasPermission(#id,'EDIT')")
    void deleteStorage(long id);

    @PreAuthorize("hasPermission(#id,'EDIT')")
    StorageDto editStorage(long id, CreateElementRequest request);
}
