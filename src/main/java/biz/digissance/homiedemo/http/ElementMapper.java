package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.*;
import biz.digissance.homiedemo.http.dto.*;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ElementMapper {
    @Autowired
    private SpaceEntityRepository spaceEntityRepository;

    @Autowired
    private ElementEntityRepository elementEntityRepository;

    @SubclassMapping(target = SpaceDto.class, source = SpaceEntity.class)
    @SubclassMapping(target = RoomDto.class, source = RoomEntity.class)
    @SubclassMapping(target = StorageDto.class, source = StorageEntity.class)
    @SubclassMapping(target = ItemDto.class, source = ItemEntity.class)
    public abstract ElementDto toElementDto(ElementEntity source);

    @Mapping(target = "password", ignore = true)
    public abstract UserDto toUserDto(final UserEntity user);

    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "photoSecureURL", expression = "java(myFunc(space))")
    public abstract SpaceDto toSpaceDto(final SpaceEntity space);

    @Mapping(target = "stuff", ignore = true)
    @Mapping(target = "spaceId", expression = "java(room.getSpace().getId())")
    @Mapping(target = "photoSecureURL", expression = "java(myFunc(room))")
    public abstract RoomDto toRoomDto(final RoomEntity room);

    @Mapping(target = "stuff", ignore = true)
    @Mapping(target = "parentId", expression = "java(storage.getParent().getId())")
    @Mapping(target = "spaceId", expression = "java(storage.getSpace().getId())")
    @Mapping(target = "photoSecureURL", expression = "java(myFunc(storage))")
    public abstract StorageDto toStorageDto(final StorageEntity storage);

    @Mapping(target = "parentId", expression = "java(item.getParent().getId())")
    @Mapping(target = "spaceId", expression = "java(item.getSpace().getId())")
    @Mapping(target = "photoSecureURL", expression = "java(myFunc(item))")
    public abstract ItemDto toItemDto(final ItemEntity item);

    @Mapping(target = "owner", ignore = true)
    public abstract SpaceEntity toSpaceEntity(CreateSpaceRequest space);

    public abstract void toSpaceEntityForUpdate(final CreateElementRequest source,
                                                final @MappingTarget SpaceEntity target);

    public abstract void toRoomEntityForUpdate(final CreateElementRequest request,
                                               final @MappingTarget RoomEntity roomEntity);

    public abstract void toStorageEntityForUpdate(final CreateElementRequest request,
                                                  final @MappingTarget StorageEntity storageEntity);

    public abstract void toItemEntityForUpdate(final CreateElementRequest request,
                                               final @MappingTarget ItemEntity itemEntity);

    public RoomEntity toRoomEntity(final long parentId, final CreateElementRequest request) {
        final var parent = spaceEntityRepository.findById(parentId).orElseThrow();
        return RoomEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .space(parent)
                .owner(parent.getOwner())
                .from(parent)
                .build();
    }

    public ItemEntity toItemEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = elementEntityRepository.findById(parentId).orElseThrow();
        return ItemEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent((RoomOrStorage) parent)
                .space(parent.getSpace())
                .owner(parent.getOwner())
                .from(parent)
                .build();
    }

    public StorageEntity toStorageEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = elementEntityRepository.findById(parentId).orElseThrow();
        return StorageEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent((RoomOrStorage) parent)
                .space(parent.getSpace())
                .owner(parent.getOwner())
                .from(parent)
                .build();
    }

    public String myFunc(ElementEntity element) {
        return Optional.ofNullable(element.getPhoto())
                .map(PhotoEntity::getSecureURL).orElse(null);
    }
}
