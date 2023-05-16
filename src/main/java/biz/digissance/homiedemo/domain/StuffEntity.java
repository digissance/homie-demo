package biz.digissance.homiedemo.domain;

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

    @ToString.Exclude
    @ManyToOne(targetEntity = ElementEntity.class)
    private RoomOrStorage parent;
}
