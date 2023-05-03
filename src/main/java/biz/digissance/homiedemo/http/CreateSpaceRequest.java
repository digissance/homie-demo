package biz.digissance.homiedemo.http;

import lombok.Data;

@Data
public class CreateSpaceRequest {
    private String name;
    private String description;
    private long owner;
}
