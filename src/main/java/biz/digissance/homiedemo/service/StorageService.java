package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;

public interface StorageService {
    ItemDto createItem(long storageId, CreateElementRequest request);

    StorageDto createStorage(long parentId, CreateElementRequest request);
}
