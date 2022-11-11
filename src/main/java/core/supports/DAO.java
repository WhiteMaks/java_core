package core.supports;

import java.util.List;

public interface DAO<ENTITY, IDENTITY> {

    default void add(List<ENTITY> entities) {}

    default void update(ENTITY entity) {}

    default void update(List<ENTITY> entities) {}

    default void delete(ENTITY entity) {}

    default void delete(List<ENTITY> entity) {}

    default ENTITY add(ENTITY entity) {
        return null;
    }

    default ENTITY findById(IDENTITY id) {
        return null;
    }

    default List<ENTITY> findAll() {
        return null;
    }

}
