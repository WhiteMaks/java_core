package core.wrappers.impl;

import core.CoreFactory;
import core.supports.CustomLogger;
import core.utils.SQLResponse;
import core.wrappers.DatabaseDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExtJdbcDriver implements DatabaseDriver {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(ExtJdbcDriver.class);

    private Connection connection;

    @Override
    public void openConnection(String url, String username, String password) throws Exception {
        connection = DriverManager.getConnection(
                url,
                username,
                password
        );
        connection.setAutoCommit(false);
    }

    @Override
    public <T> List<T> select(String query, Class<T> t) {
        List<T> result = new ArrayList<T>();

        try {
            var statement = connection.createStatement();

            logger.trace("Выполнение запроса к базе [ " + query + " ]");

            statement.execute(query);

            var resultSet = statement.getResultSet();

            var response = new SQLResponse(
                    saveColumnNames(resultSet),
                    saveRows(resultSet)
            );

            result = response.mapToObjects(t);
        } catch (Exception ex) {
            try {
                result = new ArrayList<>();

                connection.rollback();

                logger.error(
                        ex.getMessage(),
                        ex
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<String> insert(List<String> queries) {
        var result = new ArrayList<String>();

        try {
            for (var query : queries) {

                var statement = connection.prepareStatement(
                        query,
                        Statement.RETURN_GENERATED_KEYS
                );

                logger.trace("Выполнение запроса к базе [ " + query + " ]");

                statement.executeUpdate();

                var rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    result.add(String.valueOf(rs.getObject(1)));
                }
            }
            connection.commit();
        } catch (Exception ex) {
            try {
                result = new ArrayList<>();

                connection.rollback();

                logger.error(
                        ex.getMessage(),
                        ex
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @Override
    public void update(List<String> queries) {
        try {
            for (var query : queries) {
                var statement = connection.prepareStatement(
                        query,
                        Statement.RETURN_GENERATED_KEYS
                );

                logger.trace("Выполнение запроса к базе [ " + query + " ]");

                statement.executeUpdate();
            }
            connection.commit();
        } catch (Exception ex) {
            try {
                connection.rollback();

                logger.error(
                        ex.getMessage(),
                        ex
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(List<String> queries) {
        update(queries);
    }

    private List<String> saveColumnNames(ResultSet resultSet) throws SQLException {
        var columns = new ArrayList<String>();

        var resultSetMetaData = resultSet.getMetaData();

        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            columns.add(resultSetMetaData.getColumnName(i));
        }

        return columns;
    }

    private List<List<Object>> saveRows(ResultSet resultSet) throws SQLException {
        var rows = new ArrayList<List<Object>>();

        var columnCount = resultSet.getMetaData()
                .getColumnCount();

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
