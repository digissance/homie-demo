package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Collection;

@Service
@Transactional
public class ElementServiceImpl implements ElementService {

    private final ElementEntityRepository repository;
    private final ElementMapper mapper;

    public ElementServiceImpl(ElementEntityRepository repository, ElementMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Collection<ElementDto> getElementPath(long id) {

        final var result = new ArrayDeque<ElementDto>();
        final var element = repository.findById(id).orElseThrow();
        result.add(mapper.toElementDto(element));
        var parent = element.getFrom();
        while (parent != null) {
            result.push(mapper.toElementDto(parent));
            parent = parent.getFrom();
        }
        return result;
    }
}
