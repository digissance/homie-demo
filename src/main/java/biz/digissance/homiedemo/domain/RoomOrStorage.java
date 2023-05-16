package biz.digissance.homiedemo.domain;

import java.util.Set;

public interface RoomOrStorage {

    Long getId();

    Set<StuffEntity> getElements();

    SpaceEntity getSpace();

    UserEntity getOwner();
}
