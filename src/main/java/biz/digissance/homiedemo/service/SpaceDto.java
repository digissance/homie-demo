package biz.digissance.homiedemo.service;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SpaceDto extends ElementDto {

    private Set<RoomDto> rooms = new HashSet<>();
}
