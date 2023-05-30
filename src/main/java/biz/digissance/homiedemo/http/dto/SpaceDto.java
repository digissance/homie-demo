package biz.digissance.homiedemo.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SpaceDto extends ElementDto implements SomethingHoldingElements {

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RoomDto> rooms = new HashSet<>();

    @Override
    @JsonIgnore
    public Set<ElementDto> getElements() {
        return new HashSet<>(rooms);
    }
}
