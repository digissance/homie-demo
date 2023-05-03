package biz.digissance.homiedemo.http;

import lombok.Data;

@Data
public class CreateElementRequest {
    private String name;
    private String description;
}
