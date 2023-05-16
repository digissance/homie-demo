package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.bdd.requests.ItemRequest;
import biz.digissance.homiedemo.bdd.requests.RoomRequest;
import biz.digissance.homiedemo.bdd.requests.SpaceRequest;
import biz.digissance.homiedemo.bdd.requests.StorageRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.function.Function;
import org.springframework.boot.test.web.client.TestRestTemplate;

public enum ElementType implements ElementTypeFactory {
    SPACE(new Function<>() {
        @Override
        public ElementRequest apply(final ElementDto elementDto) {
            return new SpaceRequest(SPACE.restTemplate, SPACE.cache, elementDto.getName(), elementDto.getDescription());
        }
    }), ROOM(new Function<>() {
        @Override
        public ElementRequest apply(final ElementDto elementDto) {
            return new RoomRequest(ROOM.restTemplate, ROOM.cache, elementDto.getName(), elementDto.getDescription(),
                    null);
        }
    }), STORAGE(new Function<>() {
        @Override
        public ElementRequest apply(final ElementDto elementDto) {
            return new StorageRequest(STORAGE.restTemplate, STORAGE.cache, elementDto.getName(),
                    elementDto.getDescription(),
                    null);
        }
    }), ITEM(new Function<>() {
        @Override
        public ElementRequest apply(final ElementDto elementDto) {
            return new ItemRequest(ITEM.restTemplate, ITEM.cache, elementDto.getName(), elementDto.getDescription(),
                    null);
        }
    });

    ElementType(final Function<ElementDto, ElementRequest> buildRequest) {
        this.buildRequest = buildRequest;
    }

    public static ElementType getType(ElementDto elementDto) {
        if (elementDto instanceof SpaceDto) {
            return SPACE;
        }
        if (elementDto instanceof RoomDto) {
            return ROOM;
        }
        if (elementDto instanceof StorageDto) {
            return STORAGE;
        }
        if (elementDto instanceof ItemDto) {
            return ITEM;
        }
        throw new IllegalStateException();
    }

    private TestRestTemplate restTemplate;
    private MyCache cache;
    private final Function<ElementDto, ElementRequest> buildRequest;

    public ElementRequest getRequest(final ElementDto elementDto) {
        return buildRequest.apply(elementDto);
    }

    public void setRestTemplate(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setCache(final MyCache cache) {
        this.cache = cache;
    }
}
