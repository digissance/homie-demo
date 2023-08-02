package biz.digissance.homiedemo.http.dto;

import java.util.List;

public interface RoomOrStorageDto {
    Long getId();

    String getName();

    List<StuffDto> getStuff();
}
