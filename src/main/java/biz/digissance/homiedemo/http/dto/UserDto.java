package biz.digissance.homiedemo.http.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
public class UserDto {

    private String identifier;
    private String username;
    @ToString.Exclude
    private String password;
}
