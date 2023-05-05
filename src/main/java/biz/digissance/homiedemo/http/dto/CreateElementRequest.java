package biz.digissance.homiedemo.http.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateElementRequest {
    private String name;
    private String description;
}
