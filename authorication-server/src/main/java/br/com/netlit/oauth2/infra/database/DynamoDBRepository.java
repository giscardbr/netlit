package br.com.netlit.oauth2.infra.database;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DynamoDBRepository<T, ID> {

    private final DynamoDBMapper mapper;
    private final Class<T> type;

    public DynamoDBRepository(final DynamoDBMapper mapper, final Class<T> type) {
        this.mapper = mapper;
        this.type = type;
    }

    public void save(final T entity) {
        this.mapper.save(entity);
    }

    public void save(final Iterable<T> entities) {
        this.mapper.batchSave(entities);
    }

    public Optional<T> findById(final ID id) {
        final T entity = this.mapper.load(this.type, id);
        return Optional.ofNullable(entity);
    }

    public List<T> findAllByIds(final Iterable<ID> ids) {
        final Map<Class<?>, List<KeyPair>> itemsToGet = new HashMap<>(1);
        final List<KeyPair> keys = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> new KeyPair().withHashKey(id))
                .collect(Collectors.toList());
        itemsToGet.put(this.type, keys);

        return this.mapper.batchLoad(itemsToGet)
                .values().stream()
                .flatMap(List::stream)
                .filter(this.type::isInstance)
                .map(this.type::cast)
                .collect(Collectors.toList());
    }

    public List<T> findAllByIds(final ID... ids) {
        return findAllByIds(Arrays.asList(ids));
    }

    public void delete(final T entity) {
        this.mapper.delete(entity);
    }

    public void delete(final Iterable<T> entities) {
        this.mapper.batchDelete(entities);
    }

    protected DynamoDBMapper getMapper() {
        return this.mapper;
    }

    protected Class<T> getType() {
        return this.type;
    }
}
