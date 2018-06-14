package dao;

import model.MyTable;
import model.Product;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    protected final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }


    public T create(T t) {
        Class<?> clazz = t.getClass();

        String tableName = getTableName(clazz);
        Field[] tFields = getFields(clazz);


        String query = getQuery(tableName, tFields, t);

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //metod read call for return
        return null;
    }

    private String getQuery(String tableName, Field[] tFields, T t) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES(");

        for (int i = 0; i < tFields.length; i++) {
            try {
                if (tFields[i].getType().getSimpleName().equals("String")) {
                    query.append("'" + tFields[i].get(t) + "', ");
                } else {
                    query.append(tFields[i].get(t) + ", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query.replace(query.length() - 2, query.length() - 1, ");");

        return query.toString();
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

    public T read(ID id) throws IllegalAccessException, InstantiationException, SQLException {

        Class<?> clazz = this.getClass();
        ParameterizedType parType = (ParameterizedType) clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parType.getActualTypeArguments()[0];

        T t = (T) firstGeneric.newInstance();

        Field[] fields = getFields(firstGeneric);


        //String query = "SELECT * FROM ? WHERE ID = 1";
        String query = "SELECT * FROM " + getTableName(firstGeneric) + " WHERE iiiiiiiiID = ?";


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        //preparedStatement.setString(1, getTableName(firstGeneric));
        preparedStatement.setLong(1, (Long) id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first();

        for (int i = 0; i < fields.length; i++) {
            fields[i].set(t, resultSet.getObject(i + 1));
        }
        return t;
    }


    public T update(T t) throws IllegalAccessException, SQLException {
        Class<?> clazz = t.getClass();

        String tableName = getTableName(clazz);
        Field[] tFields = getFields(clazz);

        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

        for (int i = 1; i < tFields.length; i++) {
            if (tFields[i].getType().getSimpleName().equals("String")) {
                query.append(tFields[i].getName() + " = '" + tFields[i].get(t) + "', ");
            } else {
                query.append(tFields[i].getName() + " = " + tFields[i].get(t) + " ");
            }
        }
        query.append("WHERE " + tFields[0].getName() + " = " + tFields[0].get(t) + ";");
        System.out.println(query.toString());

        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());


        return null;
    }

    public void delete(ID t) throws SQLException, IllegalAccessException {

        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType)clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parameterizedType.getActualTypeArguments()[0];

        String tableName = getTableName(firstGeneric);
        Field[] fields = getFields(firstGeneric);


        String query = "DELETE FROM " + tableName
                + " WHERE " + fields[0].getName() + " = " + t;

        Statement statement = connection.createStatement();
        statement.executeUpdate(query);



    }

    public List<T> readAll() throws SQLException, IllegalAccessException, InstantiationException {
        List<T> listOfT = new ArrayList<>();

        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType)clazz.getGenericSuperclass();
        Class firstGeneric = (Class) parameterizedType.getActualTypeArguments()[0];


        Field[] fields = getFields(firstGeneric);

        String tableName = getTableName(firstGeneric);

        String query = "SELECT * FROM " + tableName;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            T t = (T) firstGeneric.newInstance();
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(t, resultSet.getObject(i + 1));

            }
            listOfT.add(t);
        }

        return listOfT;
    }
}


































