package biz.digissance.homiedemo.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
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
@DiscriminatorValue("room")
public class RoomEntity extends ElementEntity implements RoomOrStorage {

    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private Set<StuffEntity> elements = new HashSet<>();

    @Override
    protected String internalCalculatePath() {
        return this.getSpace().getPath().concat("/").concat(this.getName());
    }
}
