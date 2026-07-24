package org.afdb.aikp.shared.domain;

/**
 * Base class for aggregate roots.
 *
 * @param <ID> aggregate identifier
 */
public abstract class AggregateRoot<ID extends Identifier<?>>
        extends Entity<ID> {

    protected AggregateRoot(ID id) {
        super(id);
    }

}