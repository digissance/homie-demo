package biz.digissance.homiedemo.http.dto;

import java.util.Set;

public interface RoomOrStorageDto {
    Long getId();
    Set<StuffDto> getStuff();
}
