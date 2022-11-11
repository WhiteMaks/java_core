package core.utils;

import java.util.ArrayList;
import java.util.List;

public class SQLResponse {
    private final List<Column> columns;
    private final List<Row> rows;

    public SQLResponse(List<String> columns, List<List<Object>> rows) {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            this.columns.add(new Column(columns.get(i), i));
        }

        for (List<Object> row : rows) {
            this.rows.add(new Row(row));
        }
    }

    public <T> List<T> mapToObjects(Class<T> clazz) throws Exception {
        var objectObjectMapper = new ObjectMapper<T>(clazz);
        return objectObjectMapper.map(rows);
    }

    public List<Row> getRows() {
        return rows;
    }

    public Object getColumn(String columnName, int rowIndex) {
        return rows.get(rowIndex).getDate().get(findPositionColumn(columnName));
    }

    private int findPositionColumn(String columnName) {
        return columns.stream().filter(column -> column.getName().equals(columnName)).findFirst().get().getPosition();
    }

    public final class Row {
        private final List<Object> date;

        Row(List<Object> date) {
            this.date = date;
        }

        public List<Object> getDate() {
            return date;
        }

        public Object getColumn(String columnName) {
            return date.get(findPositionColumn(columnName));
        }

        public <T> T mapToObject(Class<T> clazz) throws Exception {
            var objectMapper = new ObjectMapper<T>(clazz);
            return objectMapper.map(this);
        }

        public List<Column> getColumns() {
            return columns;
        }
    }

    public final class Column {
        private final String name;
        private final Integer position;

        Column(String name, Integer position) {
            this.name = name;
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public Integer getPosition() {
            return position;
        }
    }
}
