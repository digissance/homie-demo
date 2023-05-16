package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageEntityRepository extends JpaRepository<StorageEntity, Long> {

}
