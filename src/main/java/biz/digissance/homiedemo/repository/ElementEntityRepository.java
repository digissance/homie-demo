package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.ElementEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementEntityRepository extends JpaRepository<ElementEntity, Long> {

    List<ElementEntity> findBySpaceId(final long spaceId);
}
