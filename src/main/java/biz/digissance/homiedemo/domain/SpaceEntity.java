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
@DiscriminatorValue("space")
public class SpaceEntity extends ElementEntity {

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private UserEntity owner;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "space")
    private Set<RoomEntity> rooms;

    @Override
    protected String internalCalculatePath() {
        return "/".concat(this.owner.getId().toString().concat("/").concat(this.getName()));
    }
}
