package core.wrappers;

import core.utils.SQLRequest;
import core.utils.SQLResponse;

public interface DatabaseDriver {

    void openConnection(String url, String username, String password) throws Exception;

    void closeConnection() throws Exception;

    void openTransaction() throws Exception;

    void commitTransaction() throws Exception;

    void rollbackTransaction() throws Exception;

    String executeSQLQueryUpdate(SQLRequest sqlRequest) throws Exception;

    boolean executeSQLQuery(SQLRequest sqlRequest) throws Exception;

    boolean isConnected();

    SQLResponse getResponse() throws Exception;

    String getLastSQLRequest();

}
