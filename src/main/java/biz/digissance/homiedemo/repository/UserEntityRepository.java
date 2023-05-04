package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdentifier(String userIdentifier);
}
