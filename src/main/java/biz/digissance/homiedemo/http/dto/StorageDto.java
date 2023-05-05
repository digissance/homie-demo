package biz.digissance.homiedemo.http.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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
public class StorageDto extends StuffDto implements RoomOrStorageDto {

    @Builder.Default
    @ToString.Exclude
    private Set<StuffDto> elements = new HashSet<>();

    @Override
    public void visit(final Consumer<ElementDto> visitor) {
        visitor.accept(this);
        elements.forEach(p->p.visit(visitor));
    }
}
