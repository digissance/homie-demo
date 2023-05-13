package biz.digissance.homiedemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
    @ManyToOne(optional = false)
    private SpaceEntity space;

    @ToString.Exclude
    @ManyToOne(optional = false)
    private UserEntity owner;

    private String name;
    private String description;
    @Column(unique = true)
    private String path;

    @OneToOne
    private PhotoEntity photo;

    @PrePersist
    private void calculatePath() {
        this.path = internalCalculatePath().toLowerCase();
    }

    @PreRemove
    private void removeFromPhoto() {
        Optional.ofNullable(this.photo).ifPresent(photo -> {
            photo.setElement(null);
        });
    }

    protected abstract String internalCalculatePath();
}
