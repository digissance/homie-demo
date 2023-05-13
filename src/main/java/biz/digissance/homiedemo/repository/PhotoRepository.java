package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.PhotoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity,Long> {
    Long deleteAllByElementIsNull();

    List<PhotoEntity> findByElementIsNull();
}
