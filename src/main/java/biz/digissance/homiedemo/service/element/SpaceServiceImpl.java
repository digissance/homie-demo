package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.domain.*;
import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.*;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.RoomEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {
    private final UserEntityRepository userEntityRepository;
    private final SpaceEntityRepository spaceEntityRepository;
    private final ElementEntityRepository elementEntityRepository;
    private final RoomEntityRepository roomEntityRepository;
    private final ElementMapper mapper;
    private final Map<Class<? extends ElementEntity>, BiConsumer<ElementEntity, Map<Long, ElementDto>>> consumerMap;

    public SpaceServiceImpl(final UserEntityRepository userEntityRepository,
                            final SpaceEntityRepository spaceEntityRepository,
                            final ElementEntityRepository elementEntityRepository,
                            final RoomEntityRepository roomEntityRepository,
                            final ElementMapper mapper) {
        this.userEntityRepository = userEntityRepository;
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
    public SpaceDto createSpace(final CreateSpaceRequest request, final String owner) {
        final var spaceEntity = mapper.toSpaceEntity(request);
        spaceEntity.setOwner(userEntityRepository.findByIdentifier(owner).orElseThrow());
        spaceEntity.setSpace(spaceEntity);
        final var createdSpace = spaceEntityRepository.save(spaceEntity);
        return mapper.toSpaceDto(createdSpace);
    }

    @Override
    public RoomDto createRoom(final long spaceId, final CreateElementRequest request) {
        final var entity = mapper.toRoomEntity(spaceId, request);
        final var savedRoom = roomEntityRepository.save(entity);
        return mapper.toRoomDto(savedRoom);
    }

    @Override
    public List<RoomEntity> getRooms(final long spaceId) {
        return roomEntityRepository.findBySpaceId(spaceId);
    }

    @Override
    public SpaceDto getSpaceTree(final long spaceId) {
//        final var space = spaceEntityRepository.findById(spaceId).orElseThrow();
        Map<Long, ElementDto> elementDtoMap = new HashMap<>();
        elementEntityRepository.findBySpaceId(spaceId)
                .stream()
                .sorted(Comparator.comparing(ElementEntity::getId))
                .forEach(p -> consumerMap.get(p.getClass()).accept(p, elementDtoMap));
        return (SpaceDto) elementDtoMap.get(spaceId);
    }

    @Override
    public SpaceDto editSpace(final long spaceId, final CreateElementRequest request) {
        final var spaceEntity = spaceEntityRepository.findById(spaceId).orElseThrow();
        mapper.toSpaceEntityForUpdate(request, spaceEntity);
        return mapper.toSpaceDto(spaceEntityRepository.save(spaceEntity));
    }

    @Override
    public List<SpaceDto> getSpaces(final Authentication auth) {
        return spaceEntityRepository.findByOwnerIdentifier(auth.getName())
                .stream().map(mapper::toSpaceDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ElementDto> getElementPath(long spaceId, long elementId) {

        final var result = new ArrayDeque<ElementDto>();
        final var allSpace = elementEntityRepository.findBySpaceId(spaceId);
        final var element = allSpace.stream().filter(p -> p.getId() == elementId).findFirst().orElseThrow();
        result.add(mapper.toElementDto(element));
        var parent = element.getParent();
        while (parent != null) {
            result.push(mapper.toElementDto(parent));
            parent = parent.getParent();
        }
        return result;
    }

    private void handle(SpaceEntity space, Map<Long, ElementDto> e) {
        final var spaceDto = mapper.toSpaceDto(space);
        e.put(space.getId(), spaceDto);
    }

    private void handle(RoomEntity room, Map<Long, ElementDto> e) {
        final var roomDto = mapper.toRoomDto(room);
        e.put(room.getId(), roomDto);
        ((SpaceDto) e.get(room.getSpace().getId())).getRooms().add(roomDto);
    }

    private void handle(StorageEntity storage, Map<Long, ElementDto> e) {
        final var storageDto = mapper.toStorageDto(storage);
        e.put(storage.getId(), storageDto);
        ((RoomOrStorageDto) e.get(storage.getParent().getId())).getStuff().add(storageDto);
    }

    private void handle(ItemEntity item, Map<Long, ElementDto> e) {
        final var itemDto = mapper.toItemDto(item);
        e.put(item.getId(), itemDto);
        ((RoomOrStorageDto) e.get(item.getParent().getId())).getStuff().add(itemDto);
    }
}
