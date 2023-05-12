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
@DiscriminatorValue("space")
public class SpaceEntity extends ElementEntity {

    @ToString.Exclude
    @OneToMany(mappedBy = "space", orphanRemoval = true)
    private Set<RoomEntity> rooms;

    @Override
    protected String internalCalculatePath() {
        return "/".concat(this.getOwner().getIdentifier().concat("/").concat(this.getName()));
    }
}
