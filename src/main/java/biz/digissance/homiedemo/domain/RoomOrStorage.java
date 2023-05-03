package biz.digissance.homiedemo.domain;

import java.util.Set;

public interface RoomOrStorage {

    Long getId();

    String getPath();

    Set<StuffEntity> getElements();
}
