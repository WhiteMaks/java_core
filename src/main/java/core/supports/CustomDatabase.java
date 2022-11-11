package core.supports;

import core.CoreFactory;
import core.annotations.Id;
import core.annotations.TableName;
import core.utils.ObjectMapper;
import core.utils.SQLRequest;
import core.wrappers.DatabaseDriver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class CustomDatabase {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(CustomDatabase.class);

    private final DatabaseDriver databaseDriver;

    protected CustomDatabase(DatabaseDriver databaseDriver, String url, String username, String password) {
        this.databaseDriver = databaseDriver;
        connect(url, username, password);
    }

    public <T> List<T> selectAll(Class<T> responseClass) {
        return selectAll(null, responseClass);
    }

    public <T> List<T> selectAll(String param, Class<T> responseClass) {
        return selectAll(param, null, responseClass);
    }

    public <T> List<T> selectAll(String param, String orderByParam, Class<T> responseClass) {
        return selectAll(getCurrentTableName(responseClass), param, orderByParam, responseClass);
    }

    private <T> List<T> selectAll(String tableName, String param, String orderByParam, Class<T> responseClass) {
        List<T> result = new ArrayList<>();
        try {
            databaseDriver.executeSQLQuery(
                    new SQLRequest()
                            .select()
                            .from(tableName)
                            .where(param)
                            .orderBy(orderByParam)
            );
            logger.trace("Выполнен SQL запрос [ " + databaseDriver.getLastSQLRequest() + " ]");
            result = databaseDriver.getResponse().mapToObjects(responseClass);
        } catch (Exception ex) {
            logger.error("Ошибка выполнения SQL запроса [ " + databaseDriver.getLastSQLRequest() + " ]", ex);
        }
        logger.trace("В результате выполнения SQL запроса [ " + databaseDriver.getLastSQLRequest() + " ] найдено [ " + result.size() + " ] записей");
        return result;
    }

    public <T> T selectById(Object id, Class<T> responseClass) {
        var param = getIdParam(responseClass);
        return selectOne(param + " = " + id, responseClass);
    }

    public <T> T selectOne(Class<T> responseClass) {
        return selectOne(null, responseClass);
    }

    public <T> T selectOne(String param, Class<T> responseClass) {
        List<T> allRows = selectAll(getCurrentTableName(responseClass), param, null, responseClass);
        return allRows.size() > 0 ? allRows.get(0) : null;
    }

    public <T> T selectOne(String param, String orderByParam, Class<T> responseClass) {
        List<T> allRows = selectAll(getCurrentTableName(responseClass), param, orderByParam, responseClass);
        return allRows.size() > 0 ? allRows.get(0) : null;
    }

    public <T> T insert(Object object, Class<T> responseClass) {
        String id = null;
        try {
            Map<String, Object> unmap = ObjectMapper.unmap(object);
            id = databaseDriver.executeSQLQueryUpdate(
                    new SQLRequest()
                            .insert()
                            .into(getCurrentTableName(object.getClass()), unmap.keySet().toArray(new String[0]))
                            .values(generateValues(unmap.values()))
            );
            logger.trace("Выполнен SQL запрос [ " + databaseDriver.getLastSQLRequest() + " ]");
        } catch (Exception ex) {
            logger.error("Ошибка выполнения SQL запроса [ " + databaseDriver.getLastSQLRequest() + " ]", ex);
        }
        return selectById(id, responseClass);
    }

    public <T> void insertTransaction(List<?> objects, Class<T> responseClass) {
        try {
            logger.debug("Открытие транзакции на добавление [ " + objects.size() + " ] новых записей");
            databaseDriver.openTransaction();
            for (Object object : objects) {
                insert(object, responseClass);
            }
            logger.debug("Фиксирование транзакции на добавление [ " + objects.size() + " ] новых записей");
            databaseDriver.commitTransaction();
        } catch (Exception ex) {
            logger.error("Ошибка выполнения добавления записей [ " + objects.size() + " ] строк", ex);
            try {
                logger.debug("Откат транзакции выполнения транзакции");
                databaseDriver.rollbackTransaction();
            } catch (Exception e) {
                logger.error("Ошибка отмены транзакции...", ex);
            }
        }
    }

    public <T> int deleteById(Object id, Class<T> clazz) {
        var param = getIdParam(clazz);
        try {
            String s = databaseDriver.executeSQLQueryUpdate(
                    new SQLRequest()
                            .delete()
                            .from(getCurrentTableName(clazz))
                            .where(param + " = " + id)
            );
            return Integer.parseInt(s);
        } catch (Exception ex) {
            logger.error("Ошибка выполнения удаления записи", ex);
            return 0;
        }
    }

    private <T> String getIdParam(Class<T> clazz) {
        Optional<Field> first = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    return field.getAnnotation(Id.class) != null;
                }).findFirst();

        if (first.isPresent()) {
            return ObjectMapper.getColumnName(first.get());
        }
        return "id";
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
}
