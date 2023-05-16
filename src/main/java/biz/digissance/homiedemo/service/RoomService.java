package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoomService {
    @PreAuthorize("hasPermission(#roomId,'EDIT')")
    StorageDto createStorage(long roomId, CreateElementRequest request);

    @PreAuthorize("hasPermission(#parentId,'EDIT')")
    ItemDto createItem(long parentId, CreateElementRequest request);

    @PreAuthorize("hasPermission(#id,'EDIT')")
    void deleteRoom(long id);

    @PreAuthorize("hasPermission(#id,'EDIT')")
    RoomDto editRoom(long id, CreateElementRequest request);
}
