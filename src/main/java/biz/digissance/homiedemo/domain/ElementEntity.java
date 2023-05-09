package biz.digissance.homiedemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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

    @ManyToOne
    @ToString.Exclude
    private SpaceEntity space;

    private String name;
    private String description;
    @Column(unique = true)
    private String path;

    @PrePersist
    private void calculatePath() {
        this.path = internalCalculatePath().toLowerCase();
    }

    protected abstract String internalCalculatePath();
}
