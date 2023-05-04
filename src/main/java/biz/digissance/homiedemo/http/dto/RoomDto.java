package biz.digissance.homiedemo.http.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoomDto extends ElementDto implements RoomOrStorageDto {

    @Builder.Default
    @ToString.Exclude
    private Set<StuffDto> elements = new HashSet<>();
}
