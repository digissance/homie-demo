package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private final ElementEntityRepository repository;
    private final ElementMapper mapper;

    public StorageServiceImpl(final ElementEntityRepository repository,
                              final ElementMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ItemDto createItem(final long storageId, final CreateElementRequest request) {
        final var itemEntity = mapper.toItemEntity(storageId, request);
        return mapper.toItemDto(repository.save(itemEntity));
    }

    @Override
    public StorageDto createStorage(final long parentId, final CreateElementRequest request) {
        final var storageEntity = mapper.toStorageEntity(parentId, request);
        return mapper.toStorageDto(repository.save(storageEntity));
    }
}
