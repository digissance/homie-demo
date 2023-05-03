package biz.digissance.homiedemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
@DiscriminatorValue("room")
public class RoomEntity extends ElementEntity implements RoomOrStorage {

    @ManyToOne//(optional = false)
    @JsonIgnore
    @ToString.Exclude
    private SpaceEntity space;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private Set<StuffEntity> elements;

    @Override
    protected String internalCalculatePath() {
        return this.space.getPath().concat("/").concat(this.getName());
    }
}
