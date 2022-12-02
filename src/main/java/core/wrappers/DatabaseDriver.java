package core.wrappers;

import java.util.List;

public interface DatabaseDriver {

    void openConnection(String url, String username, String password) throws Exception;

    <T> List<T> select(String query, Class<T> t);

    List<String> insert(List<String> queries);

    void update(List<String> queries);

    void delete(List<String> queries);

}
