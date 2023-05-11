package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.repository.ItemEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemEntityRepository repository;

    public ItemServiceImpl(final ItemEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteItem(final long id) {
        repository.deleteById(id);
    }
}
