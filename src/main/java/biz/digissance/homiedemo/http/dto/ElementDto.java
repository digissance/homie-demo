package biz.digissance.homiedemo.http.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpaceDto.class, name = "Space"),
        @JsonSubTypes.Type(value = RoomDto.class, name = "Room"),
        @JsonSubTypes.Type(value = StorageDto.class, name = "Storage")
})
public abstract class ElementDto {
    private Long id;
    private String name;
    private String description;
    private String photoSecureURL;

    public final void visit(Consumer<ElementDto> visitor) {
        visitor.accept(this);
    }
}
