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
public class StorageDto extends StuffDto implements RoomOrStorageDto, SomethingHoldingElements {

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<StuffDto> stuff = new ArrayList<>();

    @Override
    @JsonIgnore
    public List<ElementDto> getElements() {
        return new ArrayList<>(stuff);
    }
}
