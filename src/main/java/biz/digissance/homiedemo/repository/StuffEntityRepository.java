package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.StuffEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StuffEntityRepository extends JpaRepository<StuffEntity, Long> {

    List<StuffEntity> findByParentId(long spaceId);
}
