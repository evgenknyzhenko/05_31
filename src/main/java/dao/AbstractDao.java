package dao;

import model.FieldsOfClass;
import model.MyTable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    protected final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T create(T t) throws SQLException, IllegalAccessException {
        Class<?> clazz = t.getClass();

        String tableName = getTableName(clazz);
        Field[] tFields = getFields(clazz);

        String query = getQueryForCreate(tableName, tFields, t);

        Statement statement = connection.createStatement();
        statement.execute(query);

        return t;
    }

    @Override
    public T read(ID id) throws IllegalAccessException, InstantiationException, SQLException {
        Class<?> clazz = this.getClass();
        ParameterizedType parType = (ParameterizedType) clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parType.getActualTypeArguments()[0];

        T t = (T) firstGeneric.newInstance();

        Field[] fields = getFields(firstGeneric);

        String query = getQueryForRead(firstGeneric, fields, id);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        resultSet.first();

        for (int i = 0; i < fields.length; i++) {
            fields[i].set(t, resultSet.getObject(i + 1));
        }

        return t;
    }

    @Override
    public T update(T t) throws IllegalAccessException, SQLException {
        Class<?> clazz = t.getClass();

        String tableName = getTableName(clazz);
        Field[] tFields = getFields(clazz);

        String query = getQueryForUpdate(tableName, tFields, t);

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);

        return t;
    }

    @Override
    public void delete(ID t) throws SQLException, IllegalAccessException {

        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parameterizedType.getActualTypeArguments()[0];

        String tableName = getTableName(firstGeneric);
        Field[] fields = getFields(firstGeneric);

        String query = getQueryForDelete(tableName, fields, t);

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    @Override
    public List<T> readAll() throws SQLException, IllegalAccessException, InstantiationException {
        List<T> listOfT = new ArrayList<>();

        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parameterizedType.getActualTypeArguments()[0];

        Field[] fields = getFields(firstGeneric);
        String tableName = getTableName(firstGeneric);

        String query = "SELECT * FROM " + tableName;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            T t = (T) firstGeneric.newInstance();
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(t, resultSet.getObject(i + 1));
            }
            listOfT.add(t);
        }
        return listOfT;
    }


    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(MyTable.class)) {
            MyTable table = clazz.getAnnotation(MyTable.class);
            String tableName = table.tableName();
            return tableName;
        } else {
            System.out.println("Name of table is absent");
            return null;
        }
    }

    private Field[] getFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
        return fields;
    }


    private String getQueryForCreate(String tableName, Field[] tFields, T t) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");

        for (int i = 0; i < tFields.length; i++) {
            if (tFields[i].isAnnotationPresent(FieldsOfClass.class)) {
                FieldsOfClass annotation = tFields[i].getAnnotation(FieldsOfClass.class);
                query.append(annotation.fieldName() + ", ");
            }
        }
        query.replace(query.length() - 2, query.length() - 1, ") VALUES (");

        for (int i = 0; i < tFields.length; i++) {
            if (tFields[i].isAnnotationPresent(FieldsOfClass.class)) {
                if (tFields[i].getType().getSimpleName().equals("String")) {
                    query.append("'" + tFields[i].get(t) + "', ");
                } else {
                    query.append(tFields[i].get(t) + ", ");
                }
            }
        }
        query.replace(query.length() - 2, query.length() - 1, ")");

        return query.toString();
    }

    private String getQueryForRead(Class firstGeneric, Field[] fields, ID id) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + getTableName(firstGeneric) + " WHERE ");

        if (fields[0].isAnnotationPresent(FieldsOfClass.class)) {
            FieldsOfClass annotation = fields[0].getAnnotation(FieldsOfClass.class);
            query.append(annotation.fieldName() + " = " + (Long) id);
        }
        return query.toString();
    }

    private String getQueryForUpdate(String tableName, Field[] tFields, T t) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 1; i < tFields.length; i++) {
            if (tFields[i].isAnnotationPresent(FieldsOfClass.class)) {
                FieldsOfClass annotation = tFields[i].getAnnotation(FieldsOfClass.class);
                if (tFields[i].getType().getSimpleName().equals("String")) {
                    query.append(annotation.fieldName() + " = '" + tFields[i].get(t) + "', ");
                } else {
                    query.append(annotation.fieldName() + " = " + tFields[i].get(t) + ", ");
                }
            }
        }
        query.replace(query.length() - 2, query.length(), " WHERE ");

        FieldsOfClass annotation = tFields[0].getAnnotation(FieldsOfClass.class);

        query.append(annotation.fieldName() + " = " + tFields[0].get(t));

        return query.toString();
    }

    private String getQueryForDelete(String tableName, Field[] fields, ID t) {
        FieldsOfClass annotation = fields[0].getAnnotation(FieldsOfClass.class);
        String query = "DELETE FROM " + tableName + " WHERE " + annotation.fieldName() + " = " + t;

        return query;
    }
}


































