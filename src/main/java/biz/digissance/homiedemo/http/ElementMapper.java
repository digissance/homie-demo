package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.ElementEntity;
import biz.digissance.homiedemo.domain.PhotoEntity;
import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;
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

    @Mapping(target = "password", ignore = true)
    public abstract UserDto toUserDto(final UserEntity user);

    @SubclassMapping(target = SpaceDto.class, source = SpaceEntity.class)
    @SubclassMapping(target = RoomDto.class, source = RoomEntity.class)
    @SubclassMapping(target = StorageDto.class, source = StorageEntity.class)
    public abstract ElementDto toElementDto(ElementEntity source);

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

    @Mapping(target = "owner", ignore = true)
    public abstract SpaceEntity toSpaceEntity(CreateSpaceRequest space);

    public RoomEntity toRoomEntity(final long parentId, final CreateElementRequest request) {
        final var parent = spaceEntityRepository.findByIdFetchOwner(parentId).orElseThrow();
        return RoomEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .space(parent)
                .owner(parent.getOwner())
                .parent(parent)
                .build();
    }

    public StorageEntity toStorageEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = elementEntityRepository.findByIdFetchAllProperties(parentId).orElseThrow();
        return StorageEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .space(parent.getSpace())
                .owner(parent.getOwner())
                .build();
    }

    public abstract void toSpaceEntityForUpdate(final CreateElementRequest source,
                                                final @MappingTarget SpaceEntity target);

    public abstract void toRoomEntityForUpdate(final CreateElementRequest request,
                                               final @MappingTarget RoomEntity roomEntity);

    public abstract void toStorageEntityForUpdate(final CreateElementRequest request,
                                                  final @MappingTarget StorageEntity storageEntity);

    public String myFunc(ElementEntity element) {
        return Optional.ofNullable(element.getPhoto())
                .map(PhotoEntity::getSecureURL).orElse(null);
    }
}
