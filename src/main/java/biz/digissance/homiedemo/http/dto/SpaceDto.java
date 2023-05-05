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
public class SpaceDto extends ElementDto {

    @Builder.Default
    @ToString.Exclude
    private Set<RoomDto> rooms = new HashSet<>();

    @Override
    public void visit(final Consumer<ElementDto> visitor) {
        visitor.accept(this);
        rooms.forEach(p->p.visit(visitor));
    }
}
