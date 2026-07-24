package org.afdb.aikp.shared.persistence;

/**
 * Generic mapper between a domain aggregate and its persistence entity.
 *
 * @param <D> Domain aggregate
 * @param <E> Persistence entity
 */
public interface PersistenceMapper<D, E> {

    /**
     * Maps a domain aggregate to a persistence entity.
     *
     * @param domain domain aggregate
     * @return persistence entity
     */
    E toEntity(D domain);

    /**
     * Maps a persistence entity to a domain aggregate.
     *
     * @param entity persistence entity
     * @return domain aggregate
     */
    D toDomain(E entity);

}