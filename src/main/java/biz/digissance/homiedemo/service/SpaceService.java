package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import biz.digissance.homiedemo.http.CreateElementRequest;
import biz.digissance.homiedemo.http.CreateSpaceRequest;
import java.util.List;

public interface SpaceService {
    SpaceEntity createSpace(CreateSpaceRequest request);

    RoomEntity createRoom(long spaceId, CreateElementRequest request);

    List<RoomEntity> getRooms(long spaceId);

    SpaceDto getSpaceTree(long spaceId);
}
