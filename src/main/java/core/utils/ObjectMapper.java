package core.utils;

import core.annotations.Column;
import core.annotations.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectMapper<T> {
    private final Class<?> clazz;
    private final Map<String, Field> fields;

    public ObjectMapper(Class<?> clazz) {
        this.clazz = clazz;
        this.fields = new HashMap<>();

        saveFieldsInMap();
    }

    public T map(SQLResponse.Row row) throws Exception {
        var object = (T) clazz.getConstructor()
                .newInstance();
        var columns = row.getColumns();
        for (SQLResponse.Column column : columns) {
            var field = fields.get(column.getName());
            if (field != null) {
                field.set(object, row.getColumn(column.getName()));
            }
        }
        return object;
    }

    public List<T> map(List<SQLResponse.Row> rows) throws Exception {
        var result = new ArrayList<T>();
        for (SQLResponse.Row row : rows) {
            result.add(map(row));
        }
        return result;
    }

    private void saveFieldsInMap() {
        var fieldList = clazz.getDeclaredFields();
        for (var field : fieldList) {
            field.setAccessible(true);
            fields.put(getColumnName(field), field);
        }
    }

    public static String getColumnName(Field field) {
        String result;
        var column = field.getAnnotation(Column.class);
        if (column != null) {
            result = column.name();
        } else {
            result = field.getName();
        }
        return result;
    }

    public static Map<String, Object> unmap(Object object) throws IllegalAccessException {
        var result = new HashMap<String, Object>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) == null) {
                result.put(getColumnName(field), field.get(object));
            }
        }
        return result;
    }
}
