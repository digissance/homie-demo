package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.ElementEntity;
import biz.digissance.homiedemo.domain.ItemEntity;
import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import biz.digissance.homiedemo.http.CreateElementRequest;
import biz.digissance.homiedemo.http.CreateSpaceRequest;
import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.RoomEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {
    private final SpaceEntityRepository spaceEntityRepository;
    private final ElementEntityRepository elementEntityRepository;
    private final RoomEntityRepository roomEntityRepository;
    private final ElementMapper mapper;
    private final Map<Class<? extends ElementEntity>, BiConsumer<ElementEntity, Map<String, ElementDto>>> consumerMap;

    public SpaceServiceImpl(final SpaceEntityRepository spaceEntityRepository,
                            final ElementEntityRepository elementEntityRepository,
                            final RoomEntityRepository roomEntityRepository,
                            final ElementMapper mapper) {
        this.spaceEntityRepository = spaceEntityRepository;
        this.elementEntityRepository = elementEntityRepository;
        this.roomEntityRepository = roomEntityRepository;
        this.mapper = mapper;
        consumerMap = Map.of(
                SpaceEntity.class, (p, e) -> handle((SpaceEntity) p, e),
                RoomEntity.class, (p, e) -> handle((RoomEntity) p, e),
                StorageEntity.class, (p, e) -> handle((StorageEntity) p, e),
                ItemEntity.class, (p, e) -> handle((ItemEntity) p, e)
        );
    }

    @Override
    public SpaceEntity createSpace(final CreateSpaceRequest request) {
        return spaceEntityRepository.save(mapper.toSpaceEntity(request));
    }

    @Override
    public RoomEntity createRoom(final long spaceId, final CreateElementRequest request) {
        return roomEntityRepository.save(mapper.toRoomEntity(spaceId, request));
    }

    @Override
    public List<RoomEntity> getRooms(final long spaceId) {
        return roomEntityRepository.findBySpaceId(spaceId);
    }

    @Override
    public SpaceDto getSpaceTree(final long spaceId) {
        final var space = spaceEntityRepository.findById(spaceId).orElseThrow();
        Map<String, ElementDto> elementDtoMap = new HashMap<>();
        elementEntityRepository.findByPathStartingWith(space.getPath())
                .forEach(p -> consumerMap.get(p.getClass()).accept(p, elementDtoMap));
        return (SpaceDto) elementDtoMap.get(space.getPath());
    }

    private void handle(SpaceEntity space, Map<String, ElementDto> e) {
        final var spaceDto = new SpaceDto();
        spaceDto.setName(space.getName());
        spaceDto.setDescription(space.getDescription());
        spaceDto.setPath(space.getPath());
        e.put(space.getPath(), spaceDto);
    }

    private void handle(RoomEntity room, Map<String, ElementDto> e) {
        final var roomDto = new RoomDto();
        roomDto.setName(room.getName());
        roomDto.setDescription(room.getDescription());
        roomDto.setPath(room.getPath());
        e.put(room.getPath(), roomDto);
        ((SpaceDto) e.get(room.getSpace().getPath())).getRooms().add(roomDto);
    }

    private void handle(StorageEntity storage, Map<String, ElementDto> e) {
        final var storageDto = new StorageDto();
        storageDto.setName(storage.getName());
        storageDto.setDescription(storage.getDescription());
        storageDto.setPath(storage.getPath());
        e.put(storage.getPath(), storageDto);
        ((RoomOrStorageDto) e.get(storage.getParent().getPath())).getStuff().add(storageDto);
    }

    private void handle(ItemEntity item, Map<String, ElementDto> e) {
        final var itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setPath(item.getPath());
        e.put(item.getPath(), itemDto);
        ((RoomOrStorageDto) e.get(item.getParent().getPath())).getStuff().add(itemDto);
    }
}
