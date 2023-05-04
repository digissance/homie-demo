package biz.digissance.homiedemo.http.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StorageDto extends StuffDto implements RoomOrStorageDto {

    @Builder.Default
    private Set<StuffDto> stuff = new HashSet<>();
}
