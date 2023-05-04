package biz.digissance.homiedemo.http.dto;

import lombok.Data;

@Data
public class CreateElementRequest {
    private String name;
    private String description;
}
