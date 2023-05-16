package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.RoomEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private final ElementEntityRepository repository;
    private final RoomEntityRepository roomRepository;
    private final ElementMapper mapper;

    public RoomServiceImpl(final ElementEntityRepository repository,
                           final RoomEntityRepository roomRepository,
                           final ElementMapper mapper) {
        this.repository = repository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    @Override
    public StorageDto createStorage(final long roomId, final CreateElementRequest request) {
        final var storageEntity = mapper.toStorageEntity(roomId, request);
        final var save = repository.save(storageEntity);
        return mapper.toStorageDto(save);
    }

    @Override
    public ItemDto createItem(final long parentId, final CreateElementRequest request) {
        final var itemEntity = mapper.toItemEntity(parentId, request);
        final var save = repository.save(itemEntity);
        return mapper.toItemDto(save);
    }

    @Override
    public void deleteRoom(final long id) {
        repository.deleteById(id);
    }

    @Override
    public RoomDto editRoom(final long id, final CreateElementRequest request) {
        final var roomEntity = roomRepository.findById(id).orElseThrow();
        mapper.toRoomEntityForUpdate(request, roomEntity);
        return mapper.toRoomDto(roomRepository.save(roomEntity));
    }
}
