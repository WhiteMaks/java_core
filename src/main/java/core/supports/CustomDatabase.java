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

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }
}
