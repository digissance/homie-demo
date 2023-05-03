package biz.digissance.homiedemo.http;

import biz.digissance.homiedemo.domain.ItemEntity;
import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.RoomOrStorage;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import biz.digissance.homiedemo.domain.UserEntity;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.SpaceEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        //uses = {DateTimeMapper.class, JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ElementMapper {

    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private SpaceEntityRepository spaceEntityRepository;
    @Autowired
    private ElementEntityRepository elementEntityRepository;

    public abstract SpaceEntity toSpaceEntity(CreateSpaceRequest space);

    public UserEntity map(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public RoomEntity toRoomEntity(final long parentId, final CreateElementRequest request) {
        final var parent = spaceEntityRepository.findById(parentId).orElseThrow();
        return RoomEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .space(parent)
                .build();
    }

    public ItemEntity toItemEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = (RoomOrStorage) elementEntityRepository.findById(parentId).orElseThrow();
        return ItemEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .build();
    }

    public StorageEntity toStorageEntity(final Long parentId, final CreateElementRequest request) {
        final var parent = (RoomOrStorage) elementEntityRepository.findById(parentId).orElseThrow();
        return StorageEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .build();
    }
}
