package biz.digissance.homiedemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
public abstract class ElementEntity extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SpaceEntity space;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserEntity owner;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private ElementEntity parent;

    private String name;
    private String description;

    @OneToOne
    private PhotoEntity photo;

    @PreRemove
    private void removeFromPhoto() {
        Optional.ofNullable(this.photo).ifPresent(photo -> {
            photo.setElement(null);
        });
    }
}
