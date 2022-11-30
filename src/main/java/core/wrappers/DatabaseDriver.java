package core.wrappers;

import java.util.List;

public interface DatabaseDriver {

    void openConnection(String url, String username, String password) throws Exception;

    <T> T insert(T t);

    <T> List<T> insert(List<T> t);

    <T> T update(T t);

    <T> List<T> update(List<T> t);

    <T> void delete(T t);

    <T> void delete(List<T> t);

    <T> T selectById(Object id, Class<T> t);

    <T> List<T> customSelect(String query, Class<T> t);

    <T> int customUpdate(String query, Class<T> t);

}
