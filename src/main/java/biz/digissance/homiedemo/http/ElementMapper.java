package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.ElementEntity;
import biz.digissance.homiedemo.domain.ItemEntity;
import biz.digissance.homiedemo.domain.PhotoEntity;
import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.RoomOrStorage;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import java.util.Optional;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.springframework.beans.factory.annotation.Autowired;

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

    public RoomEntity toRoomEntity(final long parentId, final CreateElementRequest request) {
        final var parent = spaceEntityRepository.findById(parentId).orElseThrow();
        return RoomEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .space(parent)
                .owner(parent.getOwner())
                .build();
    }

    public ItemEntity toItemEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = (RoomOrStorage) elementEntityRepository.findById(parentId).orElseThrow();
        return ItemEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .space(parent.getSpace())
                .owner(parent.getOwner())
                .build();
    }

    public StorageEntity toStorageEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = (RoomOrStorage) elementEntityRepository.findById(parentId).orElseThrow();
        return StorageEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .space(parent.getSpace())
                .owner(parent.getOwner())
                .build();
    }

    public String myFunc(ElementEntity element) {
        return Optional.ofNullable(element.getPhoto())
                .map(PhotoEntity::getSecureURL).orElse(null);
    }
}
