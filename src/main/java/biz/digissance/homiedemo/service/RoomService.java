package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;

public interface RoomService {
    StorageDto createStorage(long roomId, CreateElementRequest request);

    ItemDto createItem(long parentId, CreateElementRequest request);
}
