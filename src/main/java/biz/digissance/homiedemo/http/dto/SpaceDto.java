package biz.digissance.homiedemo.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SpaceDto extends ElementDto implements SomethingHoldingElements {

    @Builder.Default
    @ToString.Exclude
    private Set<RoomDto> rooms = new HashSet<>();

    @Override
    @JsonIgnore
    public Set<ElementDto> getElements() {
        return new HashSet<>(rooms);
    }
}
