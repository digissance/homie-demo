package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.http.ElementMapper;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.repository.ItemEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemEntityRepository repository;
    private final ElementMapper mapper;

    public ItemServiceImpl(final ItemEntityRepository repository,
                           final ElementMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void deleteItem(final long id) {
        repository.deleteById(id);
    }

    @Override
    public ItemDto editItem(final long id, final CreateElementRequest request) {
        final var itemEntity = repository.findById(id).orElseThrow();
        mapper.toItemEntityForUpdate(request, itemEntity);
        return mapper.toItemDto(repository.save(itemEntity));
    }
}
