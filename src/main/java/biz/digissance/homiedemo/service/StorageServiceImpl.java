package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.StorageEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private final ElementEntityRepository repository;
    private final StorageEntityRepository storageRepository;
    private final ElementMapper mapper;

    public StorageServiceImpl(final ElementEntityRepository repository,
                              final StorageEntityRepository storageRepository,
                              final ElementMapper mapper) {
        this.repository = repository;
        this.storageRepository = storageRepository;
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

    @Override
    public void deleteStorage(final long id) {
        repository.deleteById(id);
    }

    @Override
    public StorageDto editStorage(final long id, final CreateElementRequest request) {

        final var storageEntity = storageRepository.findById(id).orElseThrow();
        mapper.toStorageEntityForUpdate(request, storageEntity);
        return mapper.toStorageDto(storageRepository.save(storageEntity));
    }
}
