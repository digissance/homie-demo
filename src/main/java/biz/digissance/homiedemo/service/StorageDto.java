package biz.digissance.homiedemo.service;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StorageDto extends StuffDto implements RoomOrStorageDto {
    private Set<StuffDto> stuff = new HashSet<>();
}
