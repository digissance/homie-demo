package biz.digissance.homiedemo.http.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class UserDto {

    private String name;
    private String identifier;
}
