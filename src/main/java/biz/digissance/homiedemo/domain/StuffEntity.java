package biz.digissance.homiedemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public abstract class StuffEntity extends ElementEntity {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(targetEntity = ElementEntity.class)
    private RoomOrStorage parent;

    @Override
    protected String internalCalculatePath() {
        return this.parent.getPath().concat("/").concat(this.getName());
    }
}
