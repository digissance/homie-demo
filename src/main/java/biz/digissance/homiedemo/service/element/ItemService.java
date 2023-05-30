package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ItemService {

    @PreAuthorize("hasPermission(#id,'EDIT')")
    void deleteItem(long id);

    @PreAuthorize("hasPermission(#id,'EDIT')")
    ItemDto editItem(long id, CreateElementRequest request);
}
