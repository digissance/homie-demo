package biz.digissance.homiedemo.service;

import org.springframework.security.access.prepost.PreAuthorize;

public interface ItemService {

    @PreAuthorize("hasPermission(#id,'EDIT')")
    void deleteItem(long id);
}
