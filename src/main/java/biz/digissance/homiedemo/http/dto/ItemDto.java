package biz.digissance.homiedemo.http.dto;

import java.util.function.Consumer;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class ItemDto extends StuffDto {
    @Override
    public void visit(final Consumer<ElementDto> visitor) {
        visitor.accept(this);
    }
}
