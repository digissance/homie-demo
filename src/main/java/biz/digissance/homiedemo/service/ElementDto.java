package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.ItemEntity;
import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.domain.StorageEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpaceDto.class, name = "Space"),
        @JsonSubTypes.Type(value = RoomDto.class, name = "Room"),
        @JsonSubTypes.Type(value = StorageDto.class, name = "Storage"),
        @JsonSubTypes.Type(value = ItemDto.class, name = "Item")
})
public abstract class ElementDto {
    private String name;
    private String description;
    private String path;
}
