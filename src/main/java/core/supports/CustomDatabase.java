package core.supports;

import core.CoreFactory;
import core.annotations.Id;
import core.annotations.TableName;
import core.utils.ObjectMapper;
import core.wrappers.DatabaseDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class CustomDatabase {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(CustomDatabase.class);

    private final DatabaseDriver databaseDriver;

    protected CustomDatabase(DatabaseDriver databaseDriver, String url, String username, String password) {
        this.databaseDriver = databaseDriver;
        connect(url, username, password);
    }

    public <T> void delete(T object) {
        delete(Collections.singletonList(object));
    }

    public <T> void delete(List<T> objects) {
        var queries = generateQueriesForDelete(objects);

        databaseDriver.delete(queries);
    }

    public <T> void update(T object) {
        update(Collections.singletonList(object));
    }

    public <T> void update(List<T> objects) {
        var queries = generateQueriesForUpdate(objects);

        databaseDriver.update(queries);
    }

    public <T> T insert(T object) {
        var entities = insert(Collections.singletonList(object));

        if (entities.size() == 0) {
            return null;
        }

        return entities.get(0);
    }

    public <T> List<T> insert(List<T> objects) {
        var result = new ArrayList<T>();

        var queries = generateQueriesForInsert(objects);

        var ids = databaseDriver.insert(queries);

        for (int i = 0; i < ids.size(); i++) {
            var clazz = (Class<T>) objects.get(i)
                    .getClass();

            var entity = selectById(
                    Long.valueOf(ids.get(i)),
                    clazz
            );

            result.add(entity);
        }

        return result;
    }

    public <T> T selectById(Object id, Class<T> clazz) {
        var idFieldName = getIdParam(clazz);

        var rows = customSelect(
                "where " +
                        idFieldName +
                        " = " +
                        getColumnValue(id),
                clazz
        );

        if (rows.size() == 0) {
            return null;
        }

        return rows.get(0);
    }

    public <T> List<T> selectAll(Class<T> clazz) {
        return customSelect(
                "",
                clazz
        );
    }

    public <T> List<T> customSelect(String query, Class<T> clazz) {
        return databaseDriver.select(
                "select * from " + getCurrentTableName(clazz) + " " + query,
                clazz
        );
    }

    private String getColumnValue(Object value) {
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        return "'" + value + "'";
    }

    private <T> String getIdParam(Class<T> clazz) {
        var first = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    return field.getAnnotation(Id.class) != null;
                }).findFirst();

        if (first.isPresent()) {
            return ObjectMapper.getColumnName(first.get());
        }
        return "id";
    }

    private String getCurrentTableName(Class<?> responseClass) {
        String tableName;
        TableName annotation = responseClass.getAnnotation(TableName.class);
        if (annotation != null) {
            tableName = annotation.name();
        } else {
            tableName = responseClass.getName();
        }
        return tableName;
    }

    private void connect(String url, String username, String password) {
        for (int i = 0; i < 60; i++) {
            try {
                databaseDriver.openConnection(url, username, password);
                logger.info("Соединение с базой данных по url [ " + url + " ] установлено");
                break;
            } catch (Exception ex) {
                logger.error("Соединение с базой данных по url [ " + url + " ] не установлено!", ex);
                Common.sleep(2000);
            }
        }
    }

    private void addArrayInQuery(StringBuilder builder, String[] array) {
        for (var data : array) {
            if (data != null) {
                builder.append(data).append(",").append(" ");
            }
        }
    }

    private String[] generateValues(Collection<Object> objects) {
        List<String> resultList = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Number) {
                resultList.add(String.valueOf(object));
            } else {
                if (object == null) {
                    resultList.add("null");
                } else {
                    resultList.add("'" + object + "'");
                }
            }
        }
        return resultList.toArray(new String[0]);
    }

    private <T> List<String> generateQueriesForInsert(List<T> objects) {
        var result = new ArrayList<String>();

        for (T object : objects) {
            var query = new StringBuilder();

            try {
                var unmap = ObjectMapper.unmap(object);
                unmap.remove(getIdParam(object.getClass()));

                query.append("insert into ")
                        .append(getCurrentTableName(object.getClass()))
                        .append(" (");

                addArrayInQuery(query, unmap.keySet().toArray(new String[0]));

                var length = query.toString().length();

                query.delete(length - 2, length);
                query.append(")")
                        .append(" ");

                query.append("values").append("(");
                addArrayInQuery(
                        query,
                        generateValues(unmap.values())
                );

                length = query.toString().length();
                query.delete(length - 2, length);
                query.append(")");

                result.add(query.toString());
            } catch (Exception ex) {
                logger.error(
                        ex.getMessage(),
                        ex
                );
            }
        }
        return result;
    }

    private <T> List<String> generateQueriesForUpdate(List<T> objects) {
        var result = new ArrayList<String>();

        for (T object : objects) {
            var query = new StringBuilder();

            try {
                var unmap = ObjectMapper.unmap(object);

                query.append("update ")
                        .append(getCurrentTableName(object.getClass()))
                        .append(" set ");

                var idParam = getIdParam(object.getClass());
                var idValue = getColumnValue(unmap.get(idParam));

                unmap.remove(idParam);

                var array = new String[unmap.size()];

                var keySet = unmap.keySet().toArray(new String[0]);

                for (int i = 0; i < unmap.size(); i++) {
                    array[i] = keySet[i] + "=" + getColumnValue(unmap.get(keySet[i]));
                }

                addArrayInQuery(query, array);

                var length = query.toString().length();


                query.delete(length - 2, length);
                query.append(" where ")
                        .append(idParam)
                        .append(" = ")
                        .append(idValue);

                result.add(query.toString());
            } catch (Exception ex) {
                logger.error(
                        ex.getMessage(),
                        ex
                );
            }
        }
        return result;
    }

    private <T> List<String> generateQueriesForDelete(List<T> objects) {
        var result = new ArrayList<String>();

        for (T object : objects) {
            var query = new StringBuilder();

            try {
                var unmap = ObjectMapper.unmap(object);

                var idParam = getIdParam(object.getClass());
                var idValue = getColumnValue(unmap.get(idParam));

                query.append("delete ")
                        .append("from ")
                        .append(getCurrentTableName(object.getClass()))
                        .append(" where ")
                        .append(idParam)
                        .append(" = ")
                        .append(idValue);

                result.add(query.toString());
            } catch (Exception ex) {
                logger.error(
                        ex.getMessage(),
                        ex
                );
            }
        }
        return result;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }
}
