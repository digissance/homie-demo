package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.http.dto.ElementDto;

public interface ElementRequest {
    ElementDto create();

    ElementDto editName(String newName);
}
