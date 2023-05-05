package biz.digissance.homiedemo.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpaceEntity.class, name = "Space"),
        @JsonSubTypes.Type(value = RoomEntity.class, name = "Room"),
        @JsonSubTypes.Type(value = StorageEntity.class, name = "Storage"),
        @JsonSubTypes.Type(value = ItemEntity.class, name = "Item")
})
public abstract class ElementEntity extends BaseEntity {

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
