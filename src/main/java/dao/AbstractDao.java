package dao;

import com.sun.deploy.util.ArrayUtil;
import model.Camera;
import model.MyTable;
import model.Product;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    protected final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }


    public T create(T t) {
        Class<?> clazz = t.getClass();

        String tableName = getTableName(clazz);
        Field[] tFields = getAllFields(t, clazz);


        String query = getQuery(tableName, tFields, t);
        System.out.println(query);

        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String getQuery(String tableName, Field[] tFields, T t) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES(");

        for (int i = 0; i < tFields.length; i++) {
            try {
                if (tFields[i].getType().getSimpleName().equals("String")){
                    query.append("'" + tFields[i].get(t) + "', ");
                } else {
                    query.append(tFields[i].get(t) + ", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query.replace(query.length()-2, query.length()-1, ");");

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

    private Field[] getAllFields(T t, Class clazz) {
        Field[] parentFields = clazz.getSuperclass().getDeclaredFields();
        Field[] thisClassFields = clazz.getDeclaredFields();
        Field[] allFields = new Field[parentFields.length + thisClassFields.length];

        System.arraycopy(parentFields, 0, allFields, 0, parentFields.length);
        System.arraycopy(thisClassFields, 0, allFields, parentFields.length, thisClassFields.length);

        for (int i = 0; i < allFields.length; i++) {
            allFields[i].setAccessible(true);
        }

        return allFields;
    }

    public T read(ID id) {
        Class clazz = this.getClass();
        ParameterizedType parType = (ParameterizedType)clazz.getGenericSuperclass();
        Class firstGeneric = (Class)parType.getActualTypeArguments()[0];
        System.out.println(firstGeneric);

        //String query = "SELECT "



        return null;
    }

    public T update(T t) {
        return null;
    }

    public void delete(ID t) {

    }

    public List<T> readAll() {
        return null;
    }
}
