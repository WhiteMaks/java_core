package core.utils;

public class SQLRequest {
    private final StringBuilder query;

    public SQLRequest() {
        query = new StringBuilder();
    }

    public SQLRequest insert() {
        query.append("insert").append(" ");
        return this;
    }

    public SQLRequest delete() {
        query.append("delete").append(" ");
        return this;
    }

    public SQLRequest into(String tableName, String... params) {
        query.append("into").append(" ").append(tableName);
        if (params != null && params.length != 0) {
            query.append("(");
            addArrayInQuery(params);
            var length = query.toString().length();
            query.delete(length - 2, length);
            query.append(")");
        }
        query.append(" ");
        return this;
    }

    public SQLRequest values(String... values) {
        query.append("values").append("(");
        addArrayInQuery(values);
        var length = query.toString().length();
        query.delete(length - 2, length);
        query.append(")").append(" ");
        return this;
    }

    public SQLRequest select(String... columns) {
        query.append("select").append(" ");
        if (columns == null || columns.length == 0) {
            query.append("*");
        } else {
            addArrayInQuery(columns);
            var strLength = query.toString().length();
            if (strLength == 7) {
                query.append("*");
            } else {
                query.delete(strLength - 2, strLength);
            }
        }
        query.append(" ");
        return this;
    }

    public SQLRequest from(String tableName) {
        query.append("from").append(" ").append(tableName).append(" ");
        return this;
    }

    public SQLRequest where(String param) {
        if (param != null) {
            query.append("where").append(" ").append(param).append(" ");
        }
        return this;
    }

    public SQLRequest orderBy(String param) {
        if (param != null) {
            var params = param.split(" ");
            query.append("order by").append(" ").append(params[0]).append(" ").append(params[1]).append(" ");
        }
        return this;
    }

    private void addArrayInQuery(String[] array) {
        for (var data : array) {
            if (data != null) {
                query.append(data).append(",").append(" ");
            }
        }
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
