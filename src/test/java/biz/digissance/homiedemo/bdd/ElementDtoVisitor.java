package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.SomethingHoldingElements;
import java.util.function.Consumer;

record ElementDtoVisitor(Consumer<ElementDto> doYourThing) implements Consumer<ElementDto> {

    @Override
    public void accept(final ElementDto elementDto) {
        if (elementDto instanceof SomethingHoldingElements space) {
            space.getElements().forEach(p -> p.visit(this));
        }
        doYourThing.accept(elementDto);
    }
}
