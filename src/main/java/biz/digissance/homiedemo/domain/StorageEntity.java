package biz.digissance.homiedemo.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("container")
public class StorageEntity extends StuffEntity implements RoomOrStorage {

    @ToString.Exclude
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private Set<StuffEntity> elements;
}
