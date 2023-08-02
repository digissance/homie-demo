package biz.digissance.homiedemo.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SpaceDto extends ElementDto implements SomethingHoldingElements {

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RoomDto> rooms = new ArrayList<>();

    @Override
    @JsonIgnore
    public List<ElementDto> getElements() {
        return new ArrayList<>(rooms);
    }
}
