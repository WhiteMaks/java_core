package core.wrappers.impl;

import core.utils.SQLRequest;
import core.utils.SQLResponse;
import core.wrappers.DatabaseDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExtJDBCDriver implements DatabaseDriver {
    private Connection connection;
    private ResultSet resultSet;
    private SQLRequest lastRequest;

    @Override
    public void openConnection(String url, String username, String password) throws Exception {
        connection = DriverManager.getConnection(
                url,
                username,
                password
        );
    }

    @Override
    public void closeConnection() throws Exception {
        connection.close();
    }

    @Override
    public void openTransaction() throws Exception {
        connection.setAutoCommit(false);
    }

    @Override
    public void commitTransaction() throws Exception {
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void rollbackTransaction() throws Exception {
        connection.rollback();
    }

    @Override
    public String executeSQLQueryUpdate(SQLRequest sqlRequest) throws Exception {
        lastRequest = sqlRequest;
        String id = null;
        var statement = connection.prepareStatement(
                sqlRequest.toString(),
                Statement.RETURN_GENERATED_KEYS
        );
        statement.executeUpdate();
        var rs = statement.getGeneratedKeys();
        if (rs.next()) {
            id = String.valueOf(rs.getObject(1));
        }
        return id;
    }

    @Override
    public boolean executeSQLQuery(SQLRequest sqlRequest) throws Exception {
        lastRequest = sqlRequest;
        var statement = connection.createStatement();
        var result = statement.execute(sqlRequest.toString());
        resultSet = statement.getResultSet();
        return result;
    }

    @Override
    public boolean isConnected() {
        boolean closed;
        try {
            closed = connection.isClosed();
        } catch (Exception ex) {
            closed = true;
        }
        return !closed;
    }

    @Override
    public SQLResponse getResponse() throws Exception {
        return new SQLResponse(
                saveColumnNames(resultSet.getMetaData()),
                saveRows(
                        resultSet.getMetaData()
                                .getColumnCount()
                )
        );
    }

    @Override
    public String getLastSQLRequest() {
        if (lastRequest != null) {
            return lastRequest.toString();
        }
        return "";
    }

    private List<String> saveColumnNames(ResultSetMetaData resultSetMetaData) throws Exception {
        var columns = new ArrayList<String>();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            columns.add(resultSetMetaData.getColumnName(i));
        }
        return columns;
    }

    private List<List<Object>> saveRows(int columnCount) throws Exception {
        var rows = new ArrayList<List<Object>>();
        while (resultSet.next()) {
            var row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
